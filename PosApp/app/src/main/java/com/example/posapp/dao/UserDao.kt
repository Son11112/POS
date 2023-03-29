package com.example.posapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.posapp.data.UserData

@Dao
interface UserDao {

    @Query("SELECT * from user_Table ORDER BY name ASC")
    fun getItems():LiveData<List<UserData>>

    @Query("SELECT * from user_Table WHERE id = :id")
    fun getItem(id: Int): LiveData<UserData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userData: UserData)

    @Update
    suspend fun update(userData: UserData)

    @Delete
    suspend fun delete(userData: UserData)

    @Query("SELECT * FROM user_table WHERE employee_code = :employeeCode")
    suspend fun getUserByEmployeeCode(employeeCode: String): UserData

    @Query("SELECT * FROM user_table WHERE password = :password")
    suspend fun getUserByEmployeePass(password: String): UserData

    @Query("SELECT COUNT(*) FROM user_table WHERE employee_code = :employeeCode")
    fun checkEmployeeCodeExist(employeeCode: String): Int

    @Query("SELECT * FROM user_table ORDER BY name ASC")
    fun getAllUser(): LiveData<List<UserData>>

    @Query("DELETE FROM user_table WHERE id=:id")
    suspend fun deleteEmployee(id: Int)

    @Query("SELECT * FROM user_table WHERE role = 'staff'")
    fun getStaffList(): LiveData<List<UserData>>

}
