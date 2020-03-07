package com.android.myapplication.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * The Data Access Object for the [Plant] class.
 */
@Dao
interface PlantDao {
    @Query("SELECT * FROM plant")
    fun getGardenPlantings(): LiveData<List<Plant>>

    @Insert
    suspend fun insertPlant(plant: Plant): Long

    @Delete
    suspend fun deletePlant(plant: Plant)
}