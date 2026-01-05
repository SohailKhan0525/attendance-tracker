package com.sohail.attendancetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sohail.attendancetracker.data.model.AttendanceRecord
import com.sohail.attendancetracker.data.repository.AttendanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

/**
 * ViewModel for calendar view
 */
class CalendarViewModel(
    private val repository: AttendanceRepository
) : ViewModel() {
    
    private val _selectedMonth = MutableStateFlow(YearMonth.now())
    val selectedMonth: StateFlow<YearMonth> = _selectedMonth.asStateFlow()
    
    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate: StateFlow<LocalDate?> = _selectedDate.asStateFlow()
    
    private val _datesWithRecords = MutableStateFlow<Set<LocalDate>>(emptySet())
    val datesWithRecords: StateFlow<Set<LocalDate>> = _datesWithRecords.asStateFlow()
    
    private val _recordsForSelectedDate = MutableStateFlow<List<AttendanceRecord>>(emptyList())
    val recordsForSelectedDate: StateFlow<List<AttendanceRecord>> = _recordsForSelectedDate.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadDatesForMonth(_selectedMonth.value)
    }
    
    fun setMonth(yearMonth: YearMonth) {
        _selectedMonth.value = yearMonth
        loadDatesForMonth(yearMonth)
    }
    
    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        loadRecordsForDate(date)
    }
    
    fun clearSelection() {
        _selectedDate.value = null
        _recordsForSelectedDate.value = emptyList()
    }
    
    private fun loadDatesForMonth(yearMonth: YearMonth) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val startDate = yearMonth.atDay(1)
                val endDate = yearMonth.atEndOfMonth()
                
                repository.getRecordsBetweenDates(startDate, endDate).collect { records ->
                    val dates = records.map { LocalDate.parse(it.date) }.toSet()
                    _datesWithRecords.value = dates
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun loadRecordsForDate(date: LocalDate) {
        viewModelScope.launch {
            repository.getRecordsForDate(date).collect { records ->
                _recordsForSelectedDate.value = records
            }
        }
    }
    
    fun goToPreviousMonth() {
        setMonth(_selectedMonth.value.minusMonths(1))
    }
    
    fun goToNextMonth() {
        setMonth(_selectedMonth.value.plusMonths(1))
    }
    
    fun goToCurrentMonth() {
        setMonth(YearMonth.now())
    }
}
