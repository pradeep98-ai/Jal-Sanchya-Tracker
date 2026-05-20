package com.mindmatrix.jalsanchaytracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mindmatrix.jalsanchaytracker.model.SurfaceType

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val roofAreaSqFt: Double,
    val tankCapacityLitres: Double,
    val surfaceType: String
) {
    fun runoffCoefficient(): Double = SurfaceType.fromName(surfaceType).runoffCoefficient
}
