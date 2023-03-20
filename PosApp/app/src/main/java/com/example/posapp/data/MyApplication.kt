package com.example.posapp.data

import android.app.Application
import androidx.room.RoomDatabase

class MyApplication : Application() {

    val database: RoomDatabase by lazy { MyRoomDatabase.getDatabase(this) }
}