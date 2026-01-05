package com.sohail.attendancetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sohail.attendancetracker.data.model.AttendanceRecord
import com.sohail.attendancetracker.data.model.SubjectStats
import com.sohail.attendancetracker.data.repository.AttendanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for subject details screen
 */
class SubjectDetailViewModel(
    private val repository: AttendanceRepository
) : ViewModel() {
    
    private val _subjectName = MutableStateFlow("")
    val subjectName: StateFlow<String> = _subjectName.asStateFlow()
    
    private val _stats = MutableStateFlow<SubjectStats?>(null)
    val stats: StateFlow<SubjectStats?> = _stats.asStateFlow()
    
    private val _records = MutableStateFlow<List<AttendanceRecord>>(emptyList())
    val records: StateFlow<List<AttendanceRecord>> = _records.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun setSubject(subjectName: String) {
        _subjectName.value = subjectName
        loadSubjectData(subjectName)
    }
    
    private fun loadSubjectData(subjectName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _stats.value = repository.getSubjectStats(subjectName)
                repository.getRecordsForSubject(subjectName).collect { records ->
                    _records.value = records
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun refresh() {
        if (_subjectName.value.isNotEmpty()) {
            loadSubjectData(_subjectName.value)
        }
    }
}
