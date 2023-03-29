package com.example.posapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.*
import com.example.posapp.data.NotificationData

@Dao
interface NotificationDao {

    @Query("SELECT * from Notification_Table ORDER BY date ASC")
    fun getItems(): LiveData<List<NotificationData>>

    @Query("SELECT * from Notification_Table WHERE id = :id")
    fun getItem(id: Int): LiveData<NotificationData>

    @Query("SELECT * from Notification_Table ORDER BY date ASC")
    fun getNotification(): LiveData<List<NotificationData>>

    @Query("SELECT * FROM notification_table WHERE date = :selectedDate")
    suspend fun getNotificationsByDate(selectedDate: String): List<NotificationData>

    @Query("SELECT * FROM notification_table ORDER BY date DESC LIMIT 4")
    suspend fun getLatestNotification(): NotificationData?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notificationData: NotificationData)

    @Query("DELETE FROM notification_table WHERE id=:id")
    suspend fun delete(id: Int)

    @Update
    suspend fun update(notificationData: NotificationData)

}