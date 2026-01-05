package com.sohail.attendancetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sohail.attendancetracker.data.model.AttendanceRecord
import com.sohail.attendancetracker.data.model.Subjects
import com.sohail.attendancetracker.data.repository.AttendanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * ViewModel for daily attendance entry
 */
class DailyAttendanceViewModel(
    private val repository: AttendanceRepository
) : ViewModel() {
    
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()
    
    private val _periodSubjects = MutableStateFlow<Map<Int, String>>(emptyMap())
    val periodSubjects: StateFlow<Map<Int, String>> = _periodSubjects.asStateFlow()
    
    private val _periodAttendance = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val periodAttendance: StateFlow<Map<Int, Boolean>> = _periodAttendance.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved.asStateFlow()
    
    init {
        loadAttendanceForDate(_selectedDate.value)
    }
    
    fun setDate(date: LocalDate) {
        _selectedDate.value = date
        loadAttendanceForDate(date)
    }
    
    private fun loadAttendanceForDate(date: LocalDate) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val records = repository.getRecordsForDateSync(date)
                if (records.isNotEmpty()) {
                    val subjects = records.associate { it.periodNumber to it.subjectName }
                    val attendance = records.associate { it.periodNumber to it.isPresent }
                    _periodSubjects.value = subjects
                    _periodAttendance.value = attendance
                    _isSaved.value = true
                } else {
                    _periodSubjects.value = emptyMap()
                    _periodAttendance.value = emptyMap()
                    _isSaved.value = false
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun setPeriodSubject(period: Int, subject: String) {
        _periodSubjects.value = _periodSubjects.value.toMutableMap().apply {
            put(period, subject)
        }
        _isSaved.value = false
    }
    
    fun setPeriodAttendance(period: Int, isPresent: Boolean) {
        _periodAttendance.value = _periodAttendance.value.toMutableMap().apply {
            put(period, isPresent)
        }
        _isSaved.value = false
    }
    
    fun saveAttendance(onComplete: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Delete existing records for the date
                repository.deleteRecordsForDate(_selectedDate.value)
                
                // Create new records
                val records = (1..6).mapNotNull { period ->
                    val subject = _periodSubjects.value[period]
                    val attendance = _periodAttendance.value[period]
                    
                    if (subject != null && attendance != null) {
                        AttendanceRecord(
                            date = _selectedDate.value.toString(),
                            periodNumber = period,
                            subjectName = subject,
                            isPresent = attendance
                        )
                    } else {
                        null
                    }
                }
                
                if (records.isNotEmpty()) {
                    repository.insertRecords(records)
                    _isSaved.value = true
                    onComplete(true)
                } else {
                    onComplete(false)
                }
            } catch (e: Exception) {
                onComplete(false)
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearDate() {
        viewModelScope.launch {
            repository.deleteRecordsForDate(_selectedDate.value)
            _periodSubjects.value = emptyMap()
            _periodAttendance.value = emptyMap()
            _isSaved.value = false
        }
    }
    
    fun goToPreviousDay() {
        setDate(_selectedDate.value.minusDays(1))
    }
    
    fun goToNextDay() {
        setDate(_selectedDate.value.plusDays(1))
    }
    
    fun goToToday() {
        setDate(LocalDate.now())
    }
}
