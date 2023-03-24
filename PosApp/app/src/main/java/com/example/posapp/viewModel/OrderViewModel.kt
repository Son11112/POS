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


//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getTotalPriceToday(): LiveData<Int> {
//        val today = LocalDate.now()
//        val todayString = DateTimeFormatter.ISO_LOCAL_DATE.format(today)
//        return orderDao.getTotalPriceToday(todayString)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getTotalPriceWeek(): LiveData<Int> {
//        val endDate = LocalDate.now()
//        val startDate = endDate.minusDays(6)
//        val startDateString = DateTimeFormatter.ISO_LOCAL_DATE.format(startDate)
//        val endDateString = DateTimeFormatter.ISO_LOCAL_DATE.format(endDate)
//        return orderDao.getTotalPriceWeek(startDateString,endDateString )
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getTotalPriceMonth(): LiveData<Int> {
//        val endDate = LocalDate.now()
//        val startDate = endDate.minusMonths(1)
//        val startDateString = DateTimeFormatter.ISO_LOCAL_DATE.format(startDate)
//        val endDateString = DateTimeFormatter.ISO_LOCAL_DATE.format(endDate)
//        return orderDao.getTotalPriceMonth(startDateString,endDateString )
//    }
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
