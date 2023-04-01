package com.example.posapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.posapp.data.OrderFoodItem
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

    @Query("SELECT SUM(total_price) FROM orders_table WHERE date(order_date) = date('now')")
    fun getTotalPriceToday(): LiveData<Double>

    @Query("SELECT SUM(total_price) FROM orders_table WHERE date(order_date) BETWEEN date('now', 'weekday 0', '-6 days') AND date('now')")
    fun getTotalPriceWeek(): LiveData<Double>

    @Query("SELECT SUM(total_price) FROM orders_table WHERE strftime('%Y-%m', order_date) = strftime('%Y-%m', 'now')")
    fun getTotalPriceMonth(): LiveData<Double>

    @Query("DELETE FROM orders_table WHERE id = :id")
    fun deleteById(id: Int)

    @Query("UPDATE orders_table SET total_price = :totalPrice WHERE order_id = :orderId")
    suspend fun updateTotalPrice(orderId: String, totalPrice: Int)

    @Query("UPDATE orders_table SET order_status = :orderStatus WHERE id = :id")
    suspend fun updateStatus(id: Int, orderStatus: String)

    @Query("UPDATE orders_table SET order_status = :orderStatus WHERE id = :id")
    suspend fun cancelOrder(id: Int, orderStatus: String)

    @Query("SELECT * FROM orders_table WHERE order_time = :timeInMillis")
    fun getOrderByTime(timeInMillis: Long): OrdersData?

    @Query("SELECT * FROM orders_table WHERE order_status = 'on_order'")
    fun searchOrdersByStatus(): List<OrdersData>

    @Query("SELECT * FROM orders_table WHERE table_number = :tableNumber AND (order_status = 'on_order' OR order_status = 'offered') LIMIT 1")
    fun getActiveOrderByTableNumber(tableNumber: Int): LiveData<OrdersData?>

    @Query("SELECT * FROM orders_table WHERE table_number = :tableNumber AND order_status = 'unpaid' LIMIT 1")
    fun getUnpaidOrderByTableNumber(tableNumber: Int): LiveData<OrdersData?>

    @Query("SELECT * FROM orders_table WHERE order_id = :orderId")
    fun getOrderById(orderId: String): LiveData<List<OrdersData>>

}
