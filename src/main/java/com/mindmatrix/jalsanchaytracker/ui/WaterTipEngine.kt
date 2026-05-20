package com.mindmatrix.jalsanchaytracker.ui

import com.mindmatrix.jalsanchaytracker.data.UserProfile

object WaterTipEngine {
    fun tipsFor(state: DashboardState): List<String> {
        val profile = state.profile ?: return listOf(
            "Set your roof area and tank capacity first, then log today's rainfall.",
            "Start with one accurate rainfall entry. Consistent entries make the savings picture useful."
        )

        val fillPercent = state.latestTankFill * 100
        val tips = mutableListOf<String>()

        if (fillPercent > 90f) {
            tips += "Your tank is near capacity today. Check overflow routing so excess rain reaches a recharge pit or garden bed."
        } else if (fillPercent < 25f && state.entries.isNotEmpty()) {
            tips += "Today's collection is modest. Clean roof gutters and first-flush filters before the next rainfall spell."
        } else {
            tips += "Your storage is in a healthy range. Keep the inlet mesh clear to protect water quality."
        }

        if (profile.roofAreaSqFt > 1000) {
            tips += "A large roof can collect quickly. Split downpipes across filters to reduce overflow during heavy rain."
        } else {
            tips += "For smaller roofs, every clean catchment surface matters. Keep leaves and dust away from the collection path."
        }

        if (state.entries.size >= 3) {
            tips += "You have enough history for a habit loop. Compare this month's total with tank capacity to plan reuse for washing or gardening."
        } else {
            tips += "Log rainfall for a few more days to unlock a clearer monthly conservation report."
        }

        return tips
    }
}
