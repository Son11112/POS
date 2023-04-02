package com.example.posapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.*
import com.example.posapp.data.MenuData
import com.example.posapp.data.OrdersData
import com.example.posapp.data.TopSellingItem

@Dao
interface MenuDao {

    @Query("SELECT * from menu_table ORDER BY name ASC")
    fun getItems(): LiveData<List<MenuData>>

    @Query("SELECT * from menu_table WHERE id = :id")
    suspend fun getItem(id:Int): MenuData

    @Query("SELECT * FROM menu_table WHERE type = 'MAIN_FOOD'")
    fun getMainFoods(): LiveData<List<MenuData>>

    @Query("SELECT * FROM menu_table WHERE type = 'DESSERT'")
    fun getDesserts(): LiveData<List<MenuData>>

    @Query("SELECT * FROM menu_table WHERE type = 'DRINK'")
    fun getDrinks(): LiveData<List<MenuData>>

    @Query("SELECT * FROM menu_table WHERE id = :itemId")
    suspend fun getItemById(itemId: Int): MenuData

    @Query("UPDATE menu_table SET quantity_in_stock = :QuantityInStock WHERE id = :id")
    suspend fun updateQuantityInStock(id: Int, QuantityInStock: Int)

    @Query("SELECT * FROM menu_table WHERE id = :foodItemId")
    fun getMenuDataByFoodItemId(foodItemId: Int): LiveData<MenuData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(menuData: MenuData)

    @Query("SELECT * FROM menu_table")
    fun getAllMenu(): LiveData<List<MenuData>>

    @Query("DELETE FROM menu_table WHERE id=:id")
    suspend fun deleteMenu(id: Int)

    @Update
    suspend fun update(menuData: MenuData)

    @Query("""
    SELECT menu_table.id, menu_table.name, SUM(orders_food_items.quantity_in_cart) as totalSold
    FROM menu_table
    JOIN orders_food_items ON menu_table.id = orders_food_items.food_item_id
    JOIN orders_table ON orders_food_items.order_id = orders_table.order_id
    WHERE date(orders_table.order_date) BETWEEN date('now', '-30 days') AND date('now')
    GROUP BY menu_table.id
    ORDER BY totalSold DESC
    LIMIT 3
""")
    suspend fun getTopThreeSoldItems(): List<TopSellingItem>

}

