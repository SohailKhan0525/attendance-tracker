# Attendance Tracker

A modern, offline-first Android attendance tracking app for students.

## Features

- 📊 **Dashboard**: View overall and subject-wise attendance at a glance
- 📅 **Daily Marking**: Mark attendance for up to 6 periods per day
- 📖 **Subject Tracking**: Track attendance for 12 subjects (Theory, Labs, Others)
- 📆 **Calendar View**: Visual calendar showing attendance history
- 🔔 **Smart Notifications**: Get alerts when attendance drops below 72%
- 🌙 **Dark Mode**: Full light/dark theme support
- 💾 **Offline Storage**: All data stored locally with Room database
- 🎨 **Material 3 Design**: Modern, clean UI with smooth animations

## Project Context

- **College**: Sree Dattha Institute of Engineering & Science
- **Department**: Computer Science & Information Technology
- **Course**: B.Tech
- **Section**: CSIT-B
- **Regulation**: R22
- **Academic Year**: 2025–2026
- **Semester**: II Year II Semester

## Attendance Rules

- Total instructional days: 92
- Periods per day: 6
- Total periods: 552
- Minimum required attendance: 70%
- Warning threshold: 72%
- Safe bunk limit: 165 periods (~27 days)

## Subjects

### Theory (6)
1. Discrete Mathematics (DM)
2. Business Economics & Financial Analysis (BEFA)
3. Operating Systems (OS)
4. Database Management System (DBMS)
5. Java Programming
6. Constitution of India (COI)

### Labs (4)
7. Operating Systems Lab
8. Database Management System Lab
9. Java Programming Lab
10. Skills Development Lab (Node JS)

### Others (2)
11. Sports
12. Library

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: MVVM
- **Database**: Room
- **Preferences**: DataStore
- **Design**: Material 3
- **Navigation**: Compose Navigation
- **Async**: Kotlin Coroutines & Flow

## Build Instructions

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34
- Gradle 8.2.0

### Debug APK

1. Open project in Android Studio
2. Wait for Gradle sync to complete
3. Click **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
4. APK will be generated at: `app/build/outputs/apk/debug/app-debug.apk`

**OR** via command line:
```bash
./gradlew assembleDebug
```

### Release APK (Signed)

#### Option 1: Using Android Studio

1. Click **Build** → **Generate Signed Bundle / APK**
2. Select **APK** and click **Next**
3. Create a new keystore or use existing:
   - **Key store path**: Choose location (e.g., `keystore.jks`)
   - **Password**: Create strong password
   - **Key alias**: `attendance_tracker`
   - **Key password**: Same or different password
   - **Validity**: 25+ years
   - Fill in certificate details
4. Click **Next**
5. Select **release** build variant
6. Check both signature versions (V1 and V2)
7. Click **Finish**
8. APK will be at: `app/release/app-release.apk`

#### Option 2: Using Command Line

1. Create keystore:
```bash
keytool -genkey -v -keystore keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias attendance_tracker
```

2. Create `keystore.properties` in project root:
```properties
storePassword=YOUR_KEYSTORE_PASSWORD
keyPassword=YOUR_KEY_PASSWORD
keyAlias=attendance_tracker
storeFile=../keystore.jks
```

3. Add to `app/build.gradle.kts` (before `android` block):
```kotlin
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}
```

4. Add signing config in `android` block:
```kotlin
signingConfigs {
    create("release") {
        storeFile = file(keystoreProperties["storeFile"] as String)
        storePassword = keystoreProperties["storePassword"] as String
        keyAlias = keystoreProperties["keyAlias"] as String
        keyPassword = keystoreProperties["keyPassword"] as String
    }
}
```

5. Update release build type:
```kotlin
buildTypes {
    release {
        signingConfig = signingConfigs.getByName("release")
        isMinifyEnabled = true
        proguardFiles(...)
    }
}
```

6. Build:
```bash
./gradlew assembleRelease
```

### Common Issues & Fixes

#### 1. Gradle Sync Failed
- **Issue**: Dependencies not resolved
- **Fix**: 
  - Check internet connection
  - File → Invalidate Caches → Invalidate and Restart
  - Update Gradle in `gradle-wrapper.properties`

#### 2. Compilation Error: "Cannot find symbol"
- **Issue**: KSP (Room) not generating code
- **Fix**:
  - Build → Clean Project
  - Build → Rebuild Project
  - Check `app/build.gradle.kts` has KSP plugin

#### 3. APK Not Installing
- **Issue**: Signature mismatch or incompatible version
- **Fix**:
  - Uninstall old version completely
  - Check min SDK version (26 = Android 8.0)
  - Verify APK is signed correctly

#### 4. App Crashes on Launch
- **Issue**: Missing permissions or database error
- **Fix**:
  - Check Android version (need 8.0+)
  - Grant notification permission on Android 13+
  - Clear app data and reinstall

#### 5. Build Takes Too Long
- **Issue**: Gradle performance
- **Fix**:
  - Enable Gradle daemon in `gradle.properties`:
    ```properties
    org.gradle.daemon=true
    org.gradle.parallel=true
    org.gradle.caching=true
    ```
  - Increase memory: `org.gradle.jvmargs=-Xmx4096m`

## Installation

1. Enable **Developer Options** on your Android device
2. Enable **USB Debugging** 
3. Connect device or start emulator
4. Run from Android Studio or install APK:
```bash
adb install app-debug.apk
```

## Project Structure

```
app/src/main/java/com/sohail/attendancetracker/
├── data/
│   ├── dao/              # Room DAOs
│   ├── database/         # Room Database
│   ├── model/            # Data models
│   ├── preferences/      # DataStore
│   └── repository/       # Repository pattern
├── navigation/           # Navigation graph
├── notification/         # Notification system
├── ui/
│   ├── screens/          # Compose screens
│   └── theme/            # Material 3 theme
├── viewmodel/            # ViewModels
├── MainActivity.kt       # Main activity
└── AttendanceApplication.kt
```

## License

This project is for personal/educational use.

## Author

Sohail Khan - CSIT-B, SDIES

---

**Made with ❤️ for CSIT-B students**