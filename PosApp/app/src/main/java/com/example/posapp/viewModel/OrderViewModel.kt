package com.example.posapp.viewModel

import androidx.lifecycle.*
import com.example.posapp.dao.MenuDao
import com.example.posapp.dao.OrderDao
import com.example.posapp.dao.OrderFoodItemDao
import com.example.posapp.data.MenuData
import com.example.posapp.data.OrderFoodItem
import com.example.posapp.data.OrdersData
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

    suspend fun insertOrUpdateOrderItem(orderFoodItem: OrderFoodItem) {
        val existingItem =
            orderFoodItemDao.getOrderFoodItem(orderFoodItem.orderId, orderFoodItem.foodItemId)
        if (existingItem != null) {
            existingItem.quantityInCart += orderFoodItem.quantityInCart
            orderFoodItemDao.update(existingItem)
        } else {
            orderFoodItemDao.insert(orderFoodItem)
        }
    }

    suspend fun updateTotalPrice(orderId: String, totalPrice: Int) {
        withContext(Dispatchers.IO) {
            orderDao.updateTotalPrice(orderId, totalPrice)
        }
    }

    fun getUnpaidOrderByTableNumber(tableNumber: Int): LiveData<OrdersData?> {
        return orderDao.getUnpaidOrderByTableNumber(tableNumber)
    }

    fun getOrderFoodItemDetailsByOrderId(orderId: String): LiveData<List<MenuData>> {
        val orderFoodItems = orderFoodItemsByOrderId(orderId)
        val menuDataList = MediatorLiveData<List<MenuData>>()

        menuDataList.addSource(orderFoodItems) { items ->
            viewModelScope.launch {
                val menuDataItems = mutableListOf<MenuData>()
                for (item in items) {
                    val menuData = menuDao.getMenuDataByFoodItemId(item.foodItemId).value
                    if (menuData != null) {
                        menuDataItems.add(menuData)
                    }
                }
                menuDataList.postValue(menuDataItems)
            }
        }
        return menuDataList
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
