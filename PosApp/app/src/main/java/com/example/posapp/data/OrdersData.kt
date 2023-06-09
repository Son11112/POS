package com.example.posapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders_table")
data class OrdersData(
    @PrimaryKey(autoGenerate = true)
    val id: Int= 0,
    @ColumnInfo(name = "order_id")
    var orderId: String,
    @ColumnInfo(name = "order_status")
    var orderStatus: String,
    @ColumnInfo(name = "total_price")
    var totalPrice: Int,
    @ColumnInfo(name = "order_date")
    var orderDate: String,
    @ColumnInfo(name = "order_time")
    var orderTime: String,
    @ColumnInfo(name = "table_number")
    var tableNumber: Int,
    @ColumnInfo(name = "pay_method")
    var payMethod: String
)
