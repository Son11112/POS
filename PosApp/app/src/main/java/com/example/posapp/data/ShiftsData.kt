package com.example.posapp.data

import androidx.room.*

@Entity(tableName ="shifts_table")
data class ShiftsData(

    @PrimaryKey(autoGenerate = true)
    val id: Int= 0,
    @ColumnInfo(name = "name")
    val employeeName: String,
    @ColumnInfo(name = "date")
    val dateShifts: String,
    @ColumnInfo(name = "shifts")
    val timeShifts: String
)