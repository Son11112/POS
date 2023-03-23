package com.example.posapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.posapp.dao.MenuDao
import com.example.posapp.dao.OrderDao
import com.example.posapp.dao.OrderFoodItemDao
import com.example.posapp.data.MenuData
import com.example.posapp.data.OrderFoodItem
import com.example.posapp.data.OrdersData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class OrderViewModel(
    private val orderDao: OrderDao,
    private val menuDao: MenuDao,
    private val orderFoodItemDao: OrderFoodItemDao
    ): ViewModel() {

    fun getSelectedMenuWithQuantity(): LiveData<List<MenuData>> {
      return menuDao.getSelectedMenuWithQuantity()
    }

    fun getAllOrders(): LiveData<List<OrdersData>> {
        return orderDao.getAllOrders()
    }

    fun getItemByIdMenu(id: Int): LiveData<MenuData> {
        return menuDao.getItem(id)
    }

    fun getAllOrderFoodItem(): LiveData<List<OrderFoodItem>> {
        return orderFoodItemDao.getAllOrderFoodItem()
    }

    fun getOrderByTime(timeInMillis: Long): OrdersData? {
        return orderDao.getOrderByTime(timeInMillis)
    }

    fun getTotalPriceByOrderId(): LiveData<List<OrderFoodItem>> {
        return orderFoodItemDao.getAllOrderFoodItem()
    }

    fun searchOrderStatus(onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val orders = orderDao.searchOrdersByStatus()
            withContext(Dispatchers.Main) {
                if (orders.isNotEmpty()) {
                    onSuccess()
                } else {
                    onError()
                }
            }
        }
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

    fun upDateStatus(id: Int, orderStatus : String) {
        viewModelScope.launch {
            orderDao.updateStatus(id, orderStatus)
        }
    }

    fun cancelOrder(id: Int, orderStatus : String) {
        viewModelScope.launch {
            orderDao.cancelOrder(id, orderStatus)
        }
    }

    fun deleteOrder(orderId: Int, id: Int) {
        viewModelScope.launch {
            orderDao.deleteById(id)
            orderFoodItemDao.deleteByOrderId(orderId)
        }
    }

    private fun updateOrderFoodItem(ordersData: OrdersData, orderFoodItem: OrderFoodItem) {
        viewModelScope.launch {
            orderDao.update(ordersData)
            orderFoodItemDao.update(orderFoodItem)
        }
    }
}

class OrderViewModelFactory(
    private val orderDao: OrderDao,
    private val menuDao: MenuDao,
    private val orderFoodItemDao: OrderFoodItemDao,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderViewModel(orderDao, menuDao, orderFoodItemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
