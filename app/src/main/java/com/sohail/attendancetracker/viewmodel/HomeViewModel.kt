package com.sohail.attendancetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sohail.attendancetracker.data.model.OverallStats
import com.sohail.attendancetracker.data.model.SubjectStats
import com.sohail.attendancetracker.data.repository.AttendanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the home dashboard screen
 */
class HomeViewModel(
    private val repository: AttendanceRepository
) : ViewModel() {
    
    private val _overallStats = MutableStateFlow<OverallStats?>(null)
    val overallStats: StateFlow<OverallStats?> = _overallStats.asStateFlow()
    
    private val _subjectStats = MutableStateFlow<List<SubjectStats>>(emptyList())
    val subjectStats: StateFlow<List<SubjectStats>> = _subjectStats.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadStats()
    }
    
    fun loadStats() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _overallStats.value = repository.getOverallStats()
                _subjectStats.value = repository.getAllSubjectStats()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun refresh() {
        loadStats()
    }
}
