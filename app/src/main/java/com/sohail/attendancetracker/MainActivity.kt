package com.sohail.attendancetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sohail.attendancetracker.data.database.AttendanceDatabase
import com.sohail.attendancetracker.data.preferences.PreferencesManager
import com.sohail.attendancetracker.data.repository.AttendanceRepository
import com.sohail.attendancetracker.navigation.AttendanceNavHost
import com.sohail.attendancetracker.navigation.Screen
import com.sohail.attendancetracker.notification.AttendanceNotificationHelper
import com.sohail.attendancetracker.ui.theme.AttendanceTrackerTheme
import com.sohail.attendancetracker.viewmodel.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Main activity
 */
class MainActivity : ComponentActivity() {
    
    private lateinit var preferencesManager: PreferencesManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Install splash screen
        installSplashScreen()
        
        // Initialize dependencies
        val database = AttendanceDatabase.getDatabase(applicationContext)
        val repository = AttendanceRepository(database.attendanceDao())
        preferencesManager = PreferencesManager(applicationContext)
        
        // Initialize notifications
        AttendanceNotificationHelper.createNotificationChannel(this)
        
        setContent {
            val isDarkMode by preferencesManager.isDarkMode.collectAsState(initial = false)
            
            AttendanceTrackerTheme(darkTheme = isDarkMode) {
                AttendanceApp(
                    repository = repository,
                    preferencesManager = preferencesManager
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceApp(
    repository: AttendanceRepository,
    preferencesManager: PreferencesManager
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // ViewModels
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(repository)
    )
    val dailyAttendanceViewModel: DailyAttendanceViewModel = viewModel(
        factory = DailyAttendanceViewModelFactory(repository)
    )
    val subjectDetailViewModel: SubjectDetailViewModel = viewModel(
        factory = SubjectDetailViewModelFactory(repository)
    )
    val calendarViewModel: CalendarViewModel = viewModel(
        factory = CalendarViewModelFactory(repository)
    )
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(preferencesManager)
    )
    
    // Show bottom bar only on home screen
    val showBottomBar = currentRoute == Screen.Home.route
    
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text("Home") },
                        selected = true,
                        onClick = { }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Today, contentDescription = null) },
                        label = { Text("Mark") },
                        selected = false,
                        onClick = {
                            navController.navigate(Screen.DailyAttendance.route)
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                        label = { Text("Calendar") },
                        selected = false,
                        onClick = {
                            navController.navigate(Screen.Calendar.route)
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                        label = { Text("Settings") },
                        selected = false,
                        onClick = {
                            navController.navigate(Screen.Settings.route)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AttendanceNavHost(
                navController = navController,
                homeViewModel = homeViewModel,
                dailyAttendanceViewModel = dailyAttendanceViewModel,
                subjectDetailViewModel = subjectDetailViewModel,
                calendarViewModel = calendarViewModel,
                settingsViewModel = settingsViewModel
            )
        }
    }
}
