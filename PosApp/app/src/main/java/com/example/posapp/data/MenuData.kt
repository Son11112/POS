package com.example.posapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_table")
data class MenuData (
    @PrimaryKey(autoGenerate = true)
    var id: Int= 0,
    @ColumnInfo(name = "kinds")
    var productKinds : String,
    @ColumnInfo(name = "type")
    var productType : String,
    @ColumnInfo(name = "name")
    var productName : String,
    @ColumnInfo(name = "price")
    var productPrice : Int,
    @ColumnInfo(name = "quantity_in_stock")
    var productQuantity : Int,
    @ColumnInfo(name = "image")
    var productImage : ByteArray? = null

)
