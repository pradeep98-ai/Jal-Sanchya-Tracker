package com.mindmatrix.jalsanchaytracker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.mindmatrix.jalsanchaytracker.data.JalSanchayDatabase
import com.mindmatrix.jalsanchaytracker.data.JalSanchayRepository
import com.mindmatrix.jalsanchaytracker.data.RainfallEntry
import com.mindmatrix.jalsanchaytracker.data.UserProfile
import com.mindmatrix.jalsanchaytracker.model.SurfaceType
import kotlinx.coroutines.launch

class JalSanchayViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = JalSanchayRepository(JalSanchayDatabase.get(application).dao())
    private var latestProfile: UserProfile? = null
    private var latestEntries: List<RainfallEntry> = emptyList()
    private val mutableState = MediatorLiveData(DashboardState())

    val state: LiveData<DashboardState> = mutableState

    init {
        mutableState.addSource(repository.profile) {
            latestProfile = it
            publishState()
        }
        mutableState.addSource(repository.entries) {
            latestEntries = it
            publishState()
        }
    }

    fun saveProfile(roofAreaSqFt: Double, tankCapacityLitres: Double, surfaceType: SurfaceType) {
        viewModelScope.launch {
            repository.saveProfile(roofAreaSqFt, tankCapacityLitres, surfaceType)
        }
    }

    fun addRainfall(rainfallMm: Double, onMissingProfile: () -> Unit) {
        val profile = latestProfile
        if (profile == null) {
            onMissingProfile()
            return
        }

        viewModelScope.launch {
            repository.addEntry(profile, rainfallMm)
        }
    }

    fun updateRainfall(entry: RainfallEntry, rainfallMm: Double) {
        val profile = latestProfile ?: return
        viewModelScope.launch {
            repository.updateEntry(entry, profile, rainfallMm)
        }
    }

    fun deleteEntry(entry: RainfallEntry) {
        viewModelScope.launch {
            repository.deleteEntry(entry)
        }
    }

    private fun publishState() {
        mutableState.value = DashboardState(latestProfile, latestEntries)
    }
}
