package com.sohail.attendancetracker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.sohail.attendancetracker.update.UpdateChecker
import com.sohail.attendancetracker.update.UpdateInfo
import com.sohail.attendancetracker.update.UpdateResult
import com.sohail.attendancetracker.viewmodel.*

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
    val context = LocalContext.current
    
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

    var updateInfo by remember { mutableStateOf<UpdateInfo?>(null) }
    var updateError by remember { mutableStateOf<Throwable?>(null) }

    LaunchedEffect(Unit) {
        when (val result = UpdateChecker.checkForUpdates()) {
            is UpdateResult.UpdateAvailable -> updateInfo = result.info
            is UpdateResult.Error -> updateError = result.throwable
            else -> { /* no-op */ }
        }
    }

    LaunchedEffect(updateError) {
        updateError?.let {
            Toast.makeText(context, "Unable to check updates", Toast.LENGTH_SHORT).show()
            updateError = null
        }
    }
    
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

    updateInfo?.let { info ->
        UpdatePrompt(
            info = info,
            onUpdateNow = {
                openUpdateUrl(context, info.downloadUrl)
                if (!info.isForceUpdate) {
                    updateInfo = null
                }
            },
            onRemindLater = { updateInfo = null }
        )
    }
}

@Composable
private fun UpdatePrompt(
    info: UpdateInfo,
    onUpdateNow: () -> Unit,
    onRemindLater: () -> Unit
) {
    val title = if (info.isForceUpdate) "Update Required" else "Update Available"
    val description = info.message ?: "Version ${info.versionName ?: info.versionCode} is available. Download the latest build to continue."

    AlertDialog(
        onDismissRequest = {
            if (!info.isForceUpdate) {
                onRemindLater()
            }
        },
        title = { Text(title) },
        text = { Text(description) },
        confirmButton = {
            TextButton(onClick = onUpdateNow) {
                Text("Update")
            }
        },
        dismissButton = if (!info.isForceUpdate) {
            {
                TextButton(onClick = onRemindLater) {
                    Text("Later")
                }
            }
        } else null
    )
}

private fun openUpdateUrl(context: Context, url: String) {
    runCatching {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }.onFailure {
        Toast.makeText(context, "No app available to open the update link", Toast.LENGTH_LONG).show()
    }
}
