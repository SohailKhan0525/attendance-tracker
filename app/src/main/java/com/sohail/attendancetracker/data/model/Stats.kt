package com.sohail.attendancetracker.data.model

/**
 * Attendance statistics for a subject
 */
data class SubjectStats(
    val subjectName: String,
    val totalClasses: Int,
    val attendedClasses: Int,
    val percentage: Float
) {
    val canBunk: Int
        get() {
            val required = (totalClasses * 0.70).toInt()
            return (attendedClasses - required).coerceAtLeast(0)
        }
    
    val needToAttend: Int
        get() {
            val required = (totalClasses * 0.70).toInt()
            return (required - attendedClasses).coerceAtLeast(0)
        }
}

/**
 * Overall attendance statistics
 */
data class OverallStats(
    val totalPeriods: Int,
    val attendedPeriods: Int,
    val percentage: Float,
    val safeBunksRemaining: Int
) {
    val status: AttendanceStatus
        get() = when {
            percentage >= 75 -> AttendanceStatus.SAFE
            percentage >= 72 -> AttendanceStatus.WARNING
            else -> AttendanceStatus.CRITICAL
        }
}

enum class AttendanceStatus {
    SAFE,
    WARNING,
    CRITICAL
}
