package com.mindmatrix.jalsanchaytracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rainfall_entries")
data class RainfallEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateEpochDay: Long,
    val rainfallMm: Double,
    val litresSaved: Double
)
