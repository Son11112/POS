package com.example.posapp.data

import androidx.room.*

@Entity(tableName = "user_table")
data class UserData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "role")
    val role: String,
    @ColumnInfo(name = "name")
    val employeeName: String,
    @ColumnInfo(name = "employee_code")
    val employeeCode: String,
    @ColumnInfo(name = "password")
    val password: String,
    @ColumnInfo(name = "date_of_birth")
    val birth: String,
    @ColumnInfo(name = "phone_number")
    val phone: String,
)
