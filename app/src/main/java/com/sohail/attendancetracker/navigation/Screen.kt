package com.sohail.attendancetracker.navigation

/**
 * Navigation routes for the app
 */
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object DailyAttendance : Screen("daily_attendance")
    object SubjectDetail : Screen("subject_detail/{subjectName}") {
        fun createRoute(subjectName: String) = "subject_detail/$subjectName"
    }
    object Calendar : Screen("calendar")
    object Settings : Screen("settings")
}
