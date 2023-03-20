package com.example.posapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.posapp.data.MenuData
import com.example.posapp.data.OrderFoodItem

@Dao
interface OrderFoodItemDao {
    @Query("SELECT * from orders_food_items")
    fun getItems(): LiveData<List<OrderFoodItem>>

    @Query("SELECT * from orders_food_items WHERE id = :id")
    fun getItem(id:Int): LiveData<OrderFoodItem>

    @Query("SELECT * FROM orders_food_items")
    fun getAllMenu(): LiveData<List<OrderFoodItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(orderFoodItem: OrderFoodItem)

    @Query("SELECT * FROM orders_food_items")
    fun getAllOrderFoodItem(): LiveData<List<OrderFoodItem>>

    @Query("DELETE FROM orders_food_items WHERE order_id = :order_id")
    fun deleteByOrderId(order_id: Int)

    @Query("SELECT SUM(order_product_price) FROM orders_food_items WHERE order_id = :order_id")
    fun getTotalPriceByOrderId(order_id: Int): Int

    @Query("UPDATE orders_food_items SET quantity_in_cart = :quantity WHERE id = :id")
    suspend fun updateQuantityInCart(id: Int, quantity: Int)

    @Update
    suspend fun update(orderFoodItem: OrderFoodItem)

    @Delete
    suspend fun delete(orderFoodItem: OrderFoodItem)
}
