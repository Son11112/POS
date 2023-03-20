package com.example.posapp.data
import androidx.room.*

@Entity(tableName = "notification_table")
data class NotificationData (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "subject")
    val subject: String,
    @ColumnInfo(name = "detailed")
    val detailed : String
)