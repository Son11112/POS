package com.example.posapp.viewModel

import androidx.lifecycle.*
import com.example.posapp.dao.MenuDao
import com.example.posapp.dao.OrderDao
import com.example.posapp.dao.OrderFoodItemDao
import com.example.posapp.data.MenuData
import com.example.posapp.data.OrderFoodItem
import com.example.posapp.data.OrdersData
import com.example.posapp.data.TopSellingItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderViewModel(
    private val orderDao: OrderDao,
    private val orderFoodItemDao: OrderFoodItemDao,
    private val menuDao: MenuDao
) : ViewModel() {

    val totalRevenueToday = orderDao.getTotalPriceToday()
    val totalRevenueThisWeek = orderDao.getTotalPriceWeek()
    val totalRevenueThisMonth = orderDao.getTotalPriceMonth()

    fun getAllOrders(): LiveData<List<OrdersData>> {
        return orderDao.getItems()
    }

    fun insertOrder(ordersData: OrdersData) {
        viewModelScope.launch {
            orderDao.insert(ordersData)
        }
    }

    fun insertOrderItem(orderFoodItem: OrderFoodItem) {
        viewModelScope.launch {
            orderFoodItemDao.insert(orderFoodItem)
        }
    }

    fun updateOrder(order: OrdersData) {
        viewModelScope.launch {
            orderDao.updateOrder(order)
        }
    }

    fun updateStatus(id: Int, orderStatus: String) {
        viewModelScope.launch {
            orderDao.updateStatus(id, orderStatus)
        }
    }

    fun cancelOrder(id: Int, orderStatus: String) {
        viewModelScope.launch {
            orderDao.cancelOrder(id, orderStatus)
        }
    }

    fun getActiveOrderByTableNumber(tableNumber: Int): LiveData<OrdersData?> {
        return orderDao.getActiveOrderByTableNumber(tableNumber)
    }

    fun getOrderById(orderId: String): LiveData<List<OrdersData>> {
        return orderDao.getOrderById(orderId)
    }

    suspend fun getOrderFoodItemsByOrderId(orderId: String): List<OrderFoodItem> {
        return orderFoodItemDao.getOrderFoodItemsByOrderId(orderId)
    }

    fun orderFoodItemsByOrderId(orderId: String): LiveData<List<OrderFoodItem>> {
        return orderFoodItemDao.orderFoodItemsByOrderId(orderId)
    }

    suspend fun updateOrderItem(orderFoodItem: OrderFoodItem) {
        val existingItem =
            orderFoodItemDao.getOrderFoodItem(orderFoodItem.orderId, orderFoodItem.foodItemId)
        if (existingItem != null) {
            existingItem.quantityInCart += orderFoodItem.quantityInCart
            orderFoodItemDao.update(existingItem)
        }
    }

    suspend fun updateTotalPrice(orderId: String, totalPrice: Int) {
        withContext(Dispatchers.IO) {
            orderDao.updateTotalPrice(orderId, totalPrice)
        }
    }

    suspend fun getTopThreeSoldItems(): List<TopSellingItem> {
        return menuDao.getTopThreeSoldItems()
    }

}

class OrderViewModelFactory(
    private val orderDao: OrderDao,
    private val orderFoodItemDao: OrderFoodItemDao,
    private val menuDao: MenuDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderViewModel(orderDao, orderFoodItemDao, menuDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
