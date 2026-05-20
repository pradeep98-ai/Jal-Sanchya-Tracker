package com.mindmatrix.jalsanchaytracker.ui

import com.mindmatrix.jalsanchaytracker.data.RainfallEntry
import com.mindmatrix.jalsanchaytracker.data.UserProfile
import java.time.LocalDate

data class DashboardState(
    val profile: UserProfile? = null,
    val entries: List<RainfallEntry> = emptyList()
) {
    val totalLitres: Double = entries.sumOf { it.litresSaved }
    val todayLitres: Double = entries
        .filter { it.dateEpochDay == LocalDate.now().toEpochDay() }
        .sumOf { it.litresSaved }
    val waterDays: Double = totalLitres / 135.0
    val latestTankFill: Float = if (profile == null || profile.tankCapacityLitres <= 0.0) {
        0f
    } else {
        (todayLitres / profile.tankCapacityLitres).coerceIn(0.0, 1.0).toFloat()
    }
}
