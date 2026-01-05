package com.sohail.attendancetracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sohail.attendancetracker.ui.screens.*
import com.sohail.attendancetracker.viewmodel.*

/**
 * Navigation graph for the app
 */
@Composable
fun AttendanceNavHost(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    dailyAttendanceViewModel: DailyAttendanceViewModel,
    subjectDetailViewModel: SubjectDetailViewModel,
    calendarViewModel: CalendarViewModel,
    settingsViewModel: SettingsViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = homeViewModel,
                onSubjectClick = { subjectName ->
                    navController.navigate(Screen.SubjectDetail.createRoute(subjectName))
                }
            )
        }
        
        composable(Screen.DailyAttendance.route) {
            DailyAttendanceScreen(
                viewModel = dailyAttendanceViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                    homeViewModel.refresh()
                }
            )
        }
        
        composable(
            route = Screen.SubjectDetail.route,
            arguments = listOf(
                navArgument("subjectName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val subjectName = backStackEntry.arguments?.getString("subjectName") ?: ""
            SubjectDetailScreen(
                subjectName = subjectName,
                viewModel = subjectDetailViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Calendar.route) {
            CalendarScreen(
                viewModel = calendarViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
