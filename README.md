# Jal-Sanchay Tracker

Kotlin Android app for tracking household rainwater harvesting. The app stores all data locally with Room and converts rainfall into litres saved and household water days.

## What is included

- Setup inputs for roof area, tank capacity, and surface runoff type
- Daily rainfall entry with validation
- Room database persistence
- MVVM state layer with LiveData
- Water savings calculation: `roof area sq.ft * rainfall mm * 0.0929 * runoff coefficient`
- Animated tank dashboard
- Monthly conservation summary
- Shareable text report
- Offline smart tips based on setup and entry history
- Edit/delete for rainfall history

## UI Screens and Flow

1. Dashboard screen
   - Animated water tank
   - Clearly labelled litres saved today
   - Clearly labelled total litres saved
   - Clearly labelled household water days
   - Quick action to add rainfall

2. Setup screen
   - Visible roof area input in sq.ft
   - Visible tank capacity input in litres
   - Surface type selector for Tile, Concrete, and Metal Sheet
   - Runoff coefficient stored with the user profile
   - After saving, the app opens the Saved Profile screen

3. Rainfall Entry screen
   - Daily rainfall input in millimetres
   - Validation for empty, zero, negative, and non-numeric entries
   - Saves calculated litres into the local Room database
   - After saving, the app opens the Dashboard so totals are visible

4. Saved Profile screen
   - Shows the stored roof area
   - Shows the stored tank capacity
   - Shows selected surface type and runoff coefficient
   - Shows total saved so far

5. Monthly Report and Tips screen
   - Current month entry count
   - Total litres saved this month
   - Average savings per entry
   - Shareable conservation summary
   - Offline water-saving tips

6. Rainfall History screen
   - Stored rainfall entries
   - Litres saved per entry
   - Edit and delete actions

## How to test value entry

1. Open the Setup screen.
2. Enter roof area, for example `850`.
3. Enter tank capacity, for example `2000`.
4. Select a surface type and tap **Save setup and view profile**.
5. Confirm the values appear on the Saved Profile screen.
6. Open the Rainfall screen.
7. Enter rainfall, for example `24`, and tap **Calculate and save rainfall**.
8. Confirm the Dashboard shows updated litres and the History screen shows the saved entry.

## Open and run

1. Open this folder in Android Studio.
2. Let Gradle sync and install any missing Android SDK packages.
3. Run the `app` configuration on an emulator or Android device.

The project targets Android SDK 35 and supports Android 8.0+.
