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
import com.example.posapp.data.TopSellingItem

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

    @Query("UPDATE orders_food_items SET quantity_in_cart = :quantityInCart WHERE id = :id")
    suspend fun updateQuantityInCart(id: Int, quantityInCart: Int)

    @Query("UPDATE orders_food_items SET quantity_in_cart = :quantity WHERE order_id = :orderId")
    suspend fun update(orderId: String, quantity: Int)

    @Query("SELECT * FROM orders_food_items WHERE order_id = :orderId")
    suspend fun getOrderFoodItemsByOrderId(orderId: String): List<OrderFoodItem>

    @Query("SELECT * FROM orders_food_items WHERE order_id = :orderId")
    fun orderFoodItemsByOrderId(orderId: String): LiveData<List<OrderFoodItem>>

    @Delete
    suspend fun delete(orderFoodItem: OrderFoodItem)

    @Query("SELECT * FROM orders_food_items WHERE order_id = :orderId AND food_item_id = :foodItemId")
    suspend fun getOrderFoodItem(orderId: String, foodItemId: Int): OrderFoodItem?

}
