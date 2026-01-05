package com.sohail.attendancetracker.data.repository

import com.sohail.attendancetracker.data.dao.AttendanceDao
import com.sohail.attendancetracker.data.model.AttendanceRecord
import com.sohail.attendancetracker.data.model.OverallStats
import com.sohail.attendancetracker.data.model.SubjectStats
import com.sohail.attendancetracker.data.model.Subjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

/**
 * Repository for managing attendance data
 */
class AttendanceRepository(private val dao: AttendanceDao) {
    
    // Flow operations
    fun getAllRecords(): Flow<List<AttendanceRecord>> = dao.getAllRecords()
    
    fun getRecordsForDate(date: LocalDate): Flow<List<AttendanceRecord>> {
        return dao.getRecordsForDate(date.toString())
    }
    
    fun getRecordsForSubject(subjectName: String): Flow<List<AttendanceRecord>> {
        return dao.getRecordsForSubject(subjectName)
    }
    
    fun getAllDates(): Flow<List<String>> = dao.getAllDates()
    
    fun getRecordsBetweenDates(startDate: LocalDate, endDate: LocalDate): Flow<List<AttendanceRecord>> {
        return dao.getRecordsBetweenDates(startDate.toString(), endDate.toString())
    }
    
    // Suspend operations
    suspend fun insertRecord(record: AttendanceRecord): Long {
        return dao.insertRecord(record)
    }
    
    suspend fun insertRecords(records: List<AttendanceRecord>) {
        dao.insertRecords(records)
    }
    
    suspend fun updateRecord(record: AttendanceRecord) {
        dao.updateRecord(record)
    }
    
    suspend fun deleteRecord(record: AttendanceRecord) {
        dao.deleteRecord(record)
    }
    
    suspend fun getRecordsForDateSync(date: LocalDate): List<AttendanceRecord> {
        return dao.getRecordsForDateSync(date.toString())
    }
    
    suspend fun deleteRecordsForDate(date: LocalDate) {
        dao.deleteRecordsForDate(date.toString())
    }
    
    // Statistics
    suspend fun getOverallStats(): OverallStats {
        val total = dao.getTotalRecords()
        val present = dao.getTotalPresent()
        val percentage = if (total > 0) (present.toFloat() / total * 100) else 0f
        
        // Calculate safe bunks: How many more classes can be bunked while staying at 70%
        val safeBunks = if (total > 0) {
            val requiredPresent = (total * 0.70).toInt()
            (present - requiredPresent).coerceAtLeast(0)
        } else {
            0
        }
        
        return OverallStats(
            totalPeriods = total,
            attendedPeriods = present,
            percentage = percentage,
            safeBunksRemaining = safeBunks
        )
    }
    
    suspend fun getSubjectStats(subjectName: String): SubjectStats {
        val total = dao.getSubjectTotal(subjectName)
        val present = dao.getSubjectPresent(subjectName)
        val percentage = if (total > 0) (present.toFloat() / total * 100) else 0f
        
        return SubjectStats(
            subjectName = subjectName,
            totalClasses = total,
            attendedClasses = present,
            percentage = percentage
        )
    }
    
    suspend fun getAllSubjectStats(): List<SubjectStats> {
        return Subjects.allSubjects.map { subject ->
            getSubjectStats(subject.name)
        }
    }
    
    // Utility
    suspend fun hasRecordsForDate(date: LocalDate): Boolean {
        return getRecordsForDateSync(date).isNotEmpty()
    }
    
    suspend fun deleteAllRecords() {
        dao.deleteAllRecords()
    }
}
