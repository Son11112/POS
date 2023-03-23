package com.example.posapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.posapp.data.OrdersData

@Dao
interface OrderDao {
    @Query("SELECT * from orders_table")
    fun getItems(): LiveData<List<OrdersData>>

    @Query("SELECT * from orders_table WHERE id = :id")
    fun getItem(id:Int): LiveData<OrdersData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(ordersData: OrdersData)

    @Query("SELECT * FROM orders_table")
    fun getAllOrders(): LiveData<List<OrdersData>>

    @Query("DELETE FROM orders_table WHERE id = :id")
    fun deleteById(id: Int)

    @Query("UPDATE orders_table SET total_price = :totalPrice WHERE id = :id")
    suspend fun updateTotalPrice(id: Int, totalPrice: Int)

    @Query("UPDATE orders_table SET order_status = :orderStatus WHERE id = :id")
    suspend fun updateStatus(id: Int, orderStatus: String)

    @Query("UPDATE orders_table SET order_status = :orderStatus WHERE id = :id")
    suspend fun cancelOrder(id: Int, orderStatus: String)

    @Update
    suspend fun update(ordersData: OrdersData)

    @Delete
    suspend fun delete(ordersData: OrdersData)

    @Query("SELECT * FROM orders_table WHERE order_time = :timeInMillis")
    fun getOrderByTime(timeInMillis: Long): OrdersData?

    @Query("SELECT * FROM orders_table WHERE order_status = 'on_order'")
    fun searchOrdersByStatus(): List<OrdersData>
}
