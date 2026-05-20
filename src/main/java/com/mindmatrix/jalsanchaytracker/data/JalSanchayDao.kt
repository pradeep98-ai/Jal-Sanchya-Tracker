package com.mindmatrix.jalsanchaytracker.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface JalSanchayDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun observeProfile(): LiveData<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: UserProfile)

    @Query("SELECT * FROM rainfall_entries ORDER BY dateEpochDay DESC, id DESC")
    fun observeEntries(): LiveData<List<RainfallEntry>>

    @Insert
    suspend fun insertEntry(entry: RainfallEntry)

    @Update
    suspend fun updateEntry(entry: RainfallEntry)

    @Delete
    suspend fun deleteEntry(entry: RainfallEntry)
}
