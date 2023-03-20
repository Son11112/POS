package com.example.posapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.posapp.dao.*
import com.example.posapp.typeConverter.Converters
@Database(
    entities = [ShiftsData::class, UserData::class, NotificationData::class, MenuData::class, OrdersData::class, OrderFoodItem::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MyRoomDatabase : RoomDatabase() {
    abstract fun shiftsDao(): ShiftsDao
    abstract fun userDao(): UserDao
    abstract fun notificationDao(): NotificationDao
    abstract fun menuDao(): MenuDao
    abstract fun orderDao(): OrderDao
    abstract fun orderFoodItemDao(): OrderFoodItemDao

    companion object {
        @Volatile
        private var INSTANCE: MyRoomDatabase? = null
        fun getDatabase(context: Context): MyRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyRoomDatabase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
