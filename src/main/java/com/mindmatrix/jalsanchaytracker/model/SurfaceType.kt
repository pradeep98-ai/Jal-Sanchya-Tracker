package com.mindmatrix.jalsanchaytracker.model

enum class SurfaceType(val label: String, val runoffCoefficient: Double) {
    TILE("Tile", 0.60),
    CONCRETE("Concrete", 0.70),
    METAL_SHEET("Metal sheet", 0.80);

    companion object {
        fun fromName(value: String): SurfaceType = entries.firstOrNull { it.name == value } ?: CONCRETE
    }
}
