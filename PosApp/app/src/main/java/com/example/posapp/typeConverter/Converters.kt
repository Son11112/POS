package com.example.posapp.typeConverter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.TypeConverter
import com.example.posapp.data.OrderFoodItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromJson(value: String): LiveData<List<OrderFoodItem>> {
        val listType = object : TypeToken<List<OrderFoodItem>>() {}.type
        val gson = Gson()
        val data: List<OrderFoodItem> = gson.fromJson(value, listType)
        return MutableLiveData(data)
    }

    @TypeConverter
    fun toJson(data: LiveData<List<OrderFoodItem>>): String {
        val gson = Gson()
        return gson.toJson(data.value)
    }
}
