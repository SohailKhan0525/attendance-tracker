package com.sohail.attendancetracker.data.dao

import androidx.room.*
import com.sohail.attendancetracker.data.model.AttendanceRecord
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for attendance records
 */
@Dao
interface AttendanceDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: AttendanceRecord): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecords(records: List<AttendanceRecord>)
    
    @Update
    suspend fun updateRecord(record: AttendanceRecord)
    
    @Delete
    suspend fun deleteRecord(record: AttendanceRecord)
    
    @Query("SELECT * FROM attendance_records WHERE date = :date ORDER BY periodNumber")
    fun getRecordsForDate(date: String): Flow<List<AttendanceRecord>>
    
    @Query("SELECT * FROM attendance_records WHERE date = :date ORDER BY periodNumber")
    suspend fun getRecordsForDateSync(date: String): List<AttendanceRecord>
    
    @Query("SELECT * FROM attendance_records WHERE subjectName = :subjectName ORDER BY date DESC, periodNumber")
    fun getRecordsForSubject(subjectName: String): Flow<List<AttendanceRecord>>
    
    @Query("SELECT * FROM attendance_records ORDER BY date DESC, periodNumber DESC")
    fun getAllRecords(): Flow<List<AttendanceRecord>>
    
    @Query("SELECT * FROM attendance_records ORDER BY date DESC, periodNumber DESC")
    suspend fun getAllRecordsSync(): List<AttendanceRecord>
    
    @Query("SELECT DISTINCT date FROM attendance_records ORDER BY date DESC")
    fun getAllDates(): Flow<List<String>>
    
    @Query("SELECT * FROM attendance_records WHERE date >= :startDate AND date <= :endDate ORDER BY date, periodNumber")
    fun getRecordsBetweenDates(startDate: String, endDate: String): Flow<List<AttendanceRecord>>
    
    @Query("SELECT COUNT(*) FROM attendance_records WHERE isPresent = 1")
    suspend fun getTotalPresent(): Int
    
    @Query("SELECT COUNT(*) FROM attendance_records")
    suspend fun getTotalRecords(): Int

    @Query("SELECT COUNT(*) FROM attendance_records WHERE isPresent = 1 AND subjectName NOT IN (:excludedSubjects)")
    suspend fun getTotalPresentExcluding(excludedSubjects: List<String>): Int

    @Query("SELECT COUNT(*) FROM attendance_records WHERE subjectName NOT IN (:excludedSubjects)")
    suspend fun getTotalRecordsExcluding(excludedSubjects: List<String>): Int
    
    @Query("SELECT COUNT(*) FROM attendance_records WHERE subjectName = :subjectName AND isPresent = 1")
    suspend fun getSubjectPresent(subjectName: String): Int
    
    @Query("SELECT COUNT(*) FROM attendance_records WHERE subjectName = :subjectName")
    suspend fun getSubjectTotal(subjectName: String): Int
    
    @Query("DELETE FROM attendance_records WHERE date = :date")
    suspend fun deleteRecordsForDate(date: String)
    
    @Query("DELETE FROM attendance_records")
    suspend fun deleteAllRecords()
}
