package com.sohail.attendancetracker.data.model

/**
 * Subject information and configuration
 */
data class Subject(
    val name: String,
    val shortName: String,
    val type: SubjectType
)

enum class SubjectType {
    THEORY,
    LAB,
    OTHER
}

/**
 * Predefined subjects for CSIT-B, II Year II Semester
 */
object Subjects {
    val allSubjects = listOf(
        Subject("Discrete Mathematics", "DM", SubjectType.THEORY),
        Subject("Business Economics & Financial Analysis", "BEFA", SubjectType.THEORY),
        Subject("Operating Systems", "OS", SubjectType.THEORY),
        Subject("Database Management System", "DBMS", SubjectType.THEORY),
        Subject("Java Programming", "Java", SubjectType.THEORY),
        Subject("Constitution of India", "COI", SubjectType.THEORY),
        Subject("Operating Systems Lab", "OS Lab", SubjectType.LAB),
        Subject("Database Management System Lab", "DBMS Lab", SubjectType.LAB),
        Subject("Java Programming Lab", "Java Lab", SubjectType.LAB),
        Subject("Skills Development Lab (Node JS)", "Node JS", SubjectType.LAB),
        Subject("Sports", "Sports", SubjectType.OTHER),
        Subject("Library", "Library", SubjectType.OTHER)
    )
    
    /** Subjects that contribute towards attendance calculations */
    val trackableSubjects = allSubjects.filter { it.type != SubjectType.OTHER }

    /** Subjects that should be kept as options but excluded from stats */
    val excludedFromStats = allSubjects
        .filter { it.type == SubjectType.OTHER }
        .map { it.name }

    val subjectNames = allSubjects.map { it.name }
}
