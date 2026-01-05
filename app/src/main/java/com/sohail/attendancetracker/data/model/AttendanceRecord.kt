package com.sohail.attendancetracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * Represents an attendance record for a single period
 */
@Entity(tableName = "attendance_records")
data class AttendanceRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String, // Format: yyyy-MM-dd
    val periodNumber: Int, // 1-6
    val subjectName: String,
    val isPresent: Boolean,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun getLocalDate(): LocalDate = LocalDate.parse(date)
}
