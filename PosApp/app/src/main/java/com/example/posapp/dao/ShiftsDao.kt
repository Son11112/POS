package com.example.posapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.*
import com.example.posapp.data.ShiftsData

@Dao
interface ShiftsDao {

    @Query("SELECT * from shifts_Table ORDER BY name ASC")
    fun getItems(): LiveData<List<ShiftsData>>

    @Query("SELECT * from shifts_Table WHERE id = :id")
    fun getItem(id: Int): LiveData<ShiftsData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shiftsData: ShiftsData)

    @Update
    suspend fun update(shiftsData: ShiftsData)

    @Delete
    suspend fun delete(shiftsData: ShiftsData)

    @Query("SELECT * FROM shifts_table ORDER BY name ASC")
    fun getAllShifts(): LiveData<List<ShiftsData>>
}

