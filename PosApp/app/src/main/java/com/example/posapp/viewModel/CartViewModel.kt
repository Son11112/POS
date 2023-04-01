package com.example.posapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.posapp.dao.OrderDao
import com.example.posapp.dao.OrderFoodItemDao
import com.example.posapp.data.OrderFoodItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel(
    private  val orderFoodItemDao: OrderFoodItemDao,
    private  val orderDao: OrderDao
): ViewModel() {

    fun getAllMenu(): LiveData<List<OrderFoodItem>> {
        return orderFoodItemDao.getAllMenu()
    }

    suspend fun updateQuantityInCart(id: Int, quantity: Int) {
        withContext(Dispatchers.IO) {
            orderFoodItemDao.updateQuantityInCart(id, quantity)
        }
    }

    suspend fun updateTotalPrice(id: String, totalPrice: Int) {
        withContext(Dispatchers.IO) {
            orderDao.updateTotalPrice(id, totalPrice)
        }
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
}

class  CartViewModelFactory(
    private  val orderFoodItemDao: OrderFoodItemDao,
    private  val orderDao: OrderDao
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>):T{
        if (modelClass.isAssignableFrom(CartViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return CartViewModel (orderFoodItemDao, orderDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}