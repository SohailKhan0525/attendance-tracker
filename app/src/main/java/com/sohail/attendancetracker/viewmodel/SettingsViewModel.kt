package com.sohail.attendancetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sohail.attendancetracker.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for settings screen
 */
class SettingsViewModel(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()
    
    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()
    
    init {
        loadPreferences()
    }
    
    private fun loadPreferences() {
        viewModelScope.launch {
            preferencesManager.isDarkMode.collect { isDark ->
                _isDarkMode.value = isDark
            }
        }
        
        viewModelScope.launch {
            preferencesManager.areNotificationsEnabled.collect { enabled ->
                _notificationsEnabled.value = enabled
            }
        }
    }
    
    fun toggleDarkMode() {
        viewModelScope.launch {
            preferencesManager.setDarkMode(!_isDarkMode.value)
        }
    }
    
    fun toggleNotifications() {
        viewModelScope.launch {
            preferencesManager.setNotificationsEnabled(!_notificationsEnabled.value)
        }
    }
}
