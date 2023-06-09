package com.example.posapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders_food_items")

data class OrderFoodItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "order_id")
    var orderId: String,
    @ColumnInfo(name = "food_item_id")
    var foodItemId: Int = 0,
    @ColumnInfo(name = "quantity_in_cart")
    var quantityInCart: Int,

)
