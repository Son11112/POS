package com.example.posapp.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.posapp.dao.MenuDao
import com.example.posapp.dao.OrderDao
import com.example.posapp.dao.OrderFoodItemDao
import com.example.posapp.data.OrderFoodItem
import com.example.posapp.data.OrdersData
import com.example.posapp.data.TopSellingItem
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class OrderViewModel(
    private val orderDao: OrderDao,
    private val orderFoodItemDao: OrderFoodItemDao
    ): ViewModel() {

    val totalRevenueToday = orderDao.getTotalPriceToday()
    val totalRevenueThisWeek = orderDao.getTotalPriceWeek()
    val totalRevenueThisMonth = orderDao.getTotalPriceMonth()

    fun getAllOrders(): LiveData<List<OrdersData>> {
        return orderDao.getAllOrders()
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

    fun getTopSellingItemsInPeriod(startPeriod: String, endPeriod: String, numOfItems: Int): LiveData<List<TopSellingItem>> {
        return orderFoodItemDao.getTopSellingItemsInPeriod(startPeriod, endPeriod, numOfItems)
    }
}

class OrderViewModelFactory(
    private val orderDao: OrderDao,
    private val orderFoodItemDao: OrderFoodItemDao,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderViewModel(orderDao, orderFoodItemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
