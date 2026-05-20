# Jal-Sanchay Tracker

Jal-Sanchay Tracker is a Kotlin Android application for Indian households that want to measure the impact of rainwater harvesting. The app lets users enter roof area, tank capacity, roof surface type, and daily rainfall. It then calculates litres of water saved, household water days, monthly conservation summaries, and practical water-saving tips.

The app is built as an offline-first Android project using XML layouts, MVVM, LiveData, and Room database persistence.

## Table of Contents

1. Project Overview
2. Problem Statement
3. Key Features
4. UI Screens
5. User Flow
6. Calculation Logic
7. Tech Stack
8. Project Structure
9. Installation and Setup
10. How to Run
11. How to Test the App
12. Data Storage
13. Troubleshooting
14. Future Enhancements

## Project Overview

Many households install rainwater harvesting systems but do not have a simple way to know how much water they are actually collecting. Jal-Sanchay Tracker solves this by converting daily rainfall into measurable litres saved and easy-to-understand household water days.

The application focuses on:

- Simple manual rainfall entry
- Local storage without user accounts
- Clear water-saving calculations
- Visual dashboard feedback
- Practical offline recommendations
- Easy reporting and history tracking

## Problem Statement

Rainwater harvesting is useful only when families can understand its real impact. Without measurement, users often lose motivation to maintain tanks, gutters, filters, and recharge systems.

This app helps users answer:

- How many litres did my roof collect today?
- How much water have I saved in total?
- How many household water days does this represent?
- Is my tank close to filling?
- What can I do to improve collection?

## Key Features

- Setup profile for roof area, tank capacity, and surface type
- Runoff coefficient support for Tile, Concrete, and Metal Sheet roofs
- Daily rainfall entry in millimetres
- Automatic litres-saved calculation
- Animated water tank dashboard
- Total savings and household water days
- Monthly report with entry count, total savings, and average savings
- Shareable report text
- Offline smart tips based on saved data
- Rainfall history list
- Edit and delete rainfall entries
- Room database for local persistence

## UI Screens

### 1. Dashboard Screen

The Dashboard is the main overview screen.

It shows:

- Animated water tank fill level
- Litres saved today
- Total litres saved
- Household water days
- Button to quickly open the Rainfall Entry screen

### 2. Setup Screen

The Setup screen is used to save household harvesting details.

Inputs:

- Roof area in square feet
- Tank capacity in litres
- Roof surface type

Available surface types:

| Surface Type | Runoff Coefficient |
| --- | --- |
| Tile | 0.60 |
| Concrete | 0.70 |
| Metal Sheet | 0.80 |

After saving, the app opens the Saved Profile screen so the user can confirm the entered values.

### 3. Rainfall Entry Screen

The Rainfall Entry screen accepts daily rainfall in millimetres.

Validation rules:

- Empty values are rejected
- Zero values are rejected
- Negative values are rejected
- Non-numeric values are rejected

After a valid rainfall entry is saved, the app updates the Dashboard, Report, and History screens.

### 4. Saved Profile Screen

The Saved Profile screen displays the stored setup values.

It shows:

- Roof area
- Tank capacity
- Selected surface type
- Runoff coefficient
- Total litres saved so far

### 5. Report and Tips Screen

The Report and Tips screen summarizes monthly conservation performance.

It shows:

- Number of rainfall entries this month
- Total litres saved this month
- Average litres saved per entry
- Share report button
- Offline smart tips for improving harvesting

### 6. Rainfall History Screen

The Rainfall History screen lists all saved rainfall entries.

Each entry shows:

- Entry date
- Rainfall in millimetres
- Litres saved
- Edit action
- Delete action

## User Flow

1. Open the app.
2. Go to the Setup screen.
3. Enter roof area, tank capacity, and surface type.
4. Save setup.
5. Confirm values on the Saved Profile screen.
6. Go to the Rainfall Entry screen.
7. Enter rainfall in millimetres.
8. Save rainfall entry.
9. View updated water savings on the Dashboard.
10. Check monthly summary in Report and Tips.
11. View or modify old entries in Rainfall History.

## Calculation Logic

### Litres Saved

The app uses the formula:

```text
Litres Saved = Roof Area in sq.ft x Rainfall in mm x 0.0929 x Runoff Coefficient
```

Where:

- `Roof Area in sq.ft` is entered by the user
- `Rainfall in mm` is entered daily by the user
- `0.0929` converts square feet and millimetres into litres approximately
- `Runoff Coefficient` depends on roof surface type

### Total Savings

```text
Total Savings = Sum of litres saved from all rainfall entries
```

### Household Water Days

```text
Household Water Days = Total Savings / 135
```

The app uses `135 litres` as the average daily per-person water usage benchmark.

### Tank Fill Percentage

```text
Tank Fill Percentage = Today Saved Litres / Tank Capacity
```

The value is capped between `0%` and `100%`.

## Tech Stack

| Layer | Technology |
| --- | --- |
| Language | Kotlin |
| UI | XML Layouts |
| Architecture | MVVM |
| State | LiveData |
| Database | Room |
| Local Storage | SQLite through Room |
| Build Tool | Gradle |
| Minimum SDK | Android 8.0, API 26 |
| Target SDK | Android SDK 35 |

## Project Structure

```text
jalsanchaytracker/
├── app/
│   ├── build.gradle
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/mindmatrix/jalsanchaytracker/
│       │   ├── MainActivity.kt
│       │   ├── data/
│       │   │   ├── JalSanchayDao.kt
│       │   │   ├── JalSanchayDatabase.kt
│       │   │   ├── JalSanchayRepository.kt
│       │   │   ├── RainfallEntry.kt
│       │   │   └── UserProfile.kt
│       │   ├── model/
│       │   │   └── SurfaceType.kt
│       │   └── ui/
│       │       ├── DashboardState.kt
│       │       ├── JalSanchayViewModel.kt
│       │       ├── RainfallEntryAdapter.kt
│       │       ├── WaterTankView.kt
│       │       └── WaterTipEngine.kt
│       └── res/
│           ├── drawable/
│           ├── layout/
│           ├── menu/
│           ├── mipmap-anydpi-v26/
│           └── values/
├── build.gradle
├── settings.gradle
├── gradle.properties
└── README.md
```

## Installation and Setup

### Prerequisites

Install:

- Android Studio
- Android SDK
- Android emulator or physical Android device
- JDK 17 or Android Studio bundled JDK

### Open Project

1. Open Android Studio.
2. Select **Open**.
3. Choose this folder:

```text
C:\Users\deiva\AndroidStudioProjects\jalsanchaytracker
```

4. Wait for Gradle sync to finish.

## How to Run

### From Android Studio

1. Open the project.
2. Select an emulator or connected device.
3. Click **Run**.
4. Choose the `app` configuration if prompted.

### From Terminal

Use:

```powershell
.\gradlew.bat :app:assembleDebug
```

The debug APK is generated under:

```text
app/build/outputs/apk/debug/
```

## How to Test the App

Use this sample data:

| Field | Example Value |
| --- | --- |
| Roof Area | 850 sq.ft |
| Tank Capacity | 2000 litres |
| Surface Type | Concrete |
| Rainfall | 24 mm |

Expected calculation:

```text
850 x 24 x 0.0929 x 0.70 = 1326.61 litres approx.
```

Testing steps:

1. Open the Setup screen.
2. Enter `850` as roof area.
3. Enter `2000` as tank capacity.
4. Select `Concrete`.
5. Tap **Save setup and view profile**.
6. Confirm values appear on the Saved Profile screen.
7. Open the Rainfall Entry screen.
8. Enter `24`.
9. Tap **Calculate and save rainfall**.
10. Confirm Dashboard values update.
11. Open Rainfall History and confirm the entry appears.
12. Edit or delete the entry to test history actions.

## Data Storage

The app stores data locally using Room.

Stored data includes:

- User profile
- Roof area
- Tank capacity
- Surface type
- Rainfall entries
- Litres saved per entry
- Entry date

No login or cloud account is required.

## Troubleshooting

### Android Studio shows unresolved references

Try:

1. Close Android Studio.
2. Reopen the project from the root folder.
3. Run Gradle sync.
4. Use **File > Invalidate Caches > Invalidate and Restart** if errors remain.

### Gradle says JAVA_HOME is not set

Use Android Studio's bundled JDK or set `JAVA_HOME` to:

```text
C:\Program Files\Android\Android Studio\jbr
```

### App does not show saved values

Check:

1. Setup values were saved first.
2. Rainfall entry is greater than `0`.
3. You are viewing the Dashboard, Saved Profile, or History screen after saving.

### Database has old test data

Uninstall the app from the emulator or clear app storage:

```text
Settings > Apps > Jal-Sanchay Tracker > Storage > Clear Data
```

## Future Enhancements

- Unit toggle between sq.ft and sq.m
- Dark mode
- PDF export for monthly reports
- Rainfall reminders
- Graphs for weekly and monthly trends
- Multiple household profiles
- Backup and restore option
- Weather API integration for automatic rainfall import

## Project Status

Current version: `1.0`

Status: MVP Android app with local data persistence, six UI screens, rainfall calculation, dashboard, report, tips, and history.
