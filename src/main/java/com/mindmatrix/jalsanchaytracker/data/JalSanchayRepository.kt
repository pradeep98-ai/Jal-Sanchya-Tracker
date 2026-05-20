package com.mindmatrix.jalsanchaytracker.data

import com.mindmatrix.jalsanchaytracker.model.SurfaceType
import java.time.LocalDate

class JalSanchayRepository(private val dao: JalSanchayDao) {
    val profile = dao.observeProfile()
    val entries = dao.observeEntries()

    suspend fun saveProfile(roofAreaSqFt: Double, tankCapacityLitres: Double, surfaceType: SurfaceType) {
        dao.saveProfile(
            UserProfile(
                roofAreaSqFt = roofAreaSqFt,
                tankCapacityLitres = tankCapacityLitres,
                surfaceType = surfaceType.name
            )
        )
    }

    suspend fun addEntry(profile: UserProfile, rainfallMm: Double) {
        val litres = calculateLitres(profile.roofAreaSqFt, rainfallMm, profile.runoffCoefficient())
        dao.insertEntry(
            RainfallEntry(
                dateEpochDay = LocalDate.now().toEpochDay(),
                rainfallMm = rainfallMm,
                litresSaved = litres
            )
        )
    }

    suspend fun updateEntry(entry: RainfallEntry, profile: UserProfile, rainfallMm: Double) {
        dao.updateEntry(
            entry.copy(
                rainfallMm = rainfallMm,
                litresSaved = calculateLitres(profile.roofAreaSqFt, rainfallMm, profile.runoffCoefficient())
            )
        )
    }

    suspend fun deleteEntry(entry: RainfallEntry) = dao.deleteEntry(entry)

    companion object {
        fun calculateLitres(roofAreaSqFt: Double, rainfallMm: Double, runoffCoefficient: Double): Double {
            return roofAreaSqFt * rainfallMm * 0.0929 * runoffCoefficient
        }
    }
}
