package com.example.posapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_table")
data class MenuData (
    @PrimaryKey(autoGenerate = true)
    val id: Int= 0,
    @ColumnInfo(name = "kinds")
    val productKinds : String,
    @ColumnInfo(name = "type")
    val productType : String,
    @ColumnInfo(name = "name")
    val productName : String,
    @ColumnInfo(name = "price")
    val productPrice : Int,
    @ColumnInfo(name = "quantity_in_stock")
    val productQuantity : Int,
    @ColumnInfo(name = "image")
    val productImage : ByteArray? = null,
    @ColumnInfo(name = "temp_quantity_in_cart")
    var tempQuantityInCart: Int

    )
