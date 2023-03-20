package com.example.posapp.viewModel

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.*
import androidx.room.Update
import com.example.posapp.dao.MenuDao
import com.example.posapp.data.MenuData
import com.example.posapp.data.OrderFoodItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuViewModel(private val menuDao: MenuDao) : ViewModel() {

    // Key để lưu trữ Uri của ảnh trong bundle
    private val KEY_IMAGE_URI = "image_uri"

    // LiveData để lưu trữ trạng thái của ImageView
    private var _imageUri: Uri? = null

    fun getAllMenu(): LiveData<List<MenuData>> {
        return menuDao.getAllMenu()
    }

    fun getTempMenu(): LiveData<List<MenuData>> {
        return menuDao.getTempMenu()
    }


    fun getMainFoods(): LiveData<List<MenuData>> {
        return menuDao.getMainFoods()
    }

    suspend fun updateTempQuantityInCart(id: Int, tempQuantityInCart: Int) {
        withContext(Dispatchers.IO) {
            menuDao.updateTempQuantityInCart(id, tempQuantityInCart)
        }
    }

    suspend fun resetTempQuantity() {
        withContext(Dispatchers.IO) {
            menuDao.resetTempQuantity()
        }
    }

    fun getDesserts(): LiveData<List<MenuData>> {
        return menuDao.getDesserts()
    }

    fun getDrinks(): LiveData<List<MenuData>> {
        return menuDao.getDrinks()
    }

    fun addNewItem(
        productKinds: String,
        productName: String,
        productPrice: Int,
        productQuantity: Int,
        productImage: ByteArray,
        productType: String,
        tempQuantityInCart: Int
    ) {
        val newItem = MenuData(
            productKinds = productKinds,
            productName = productName,
            productPrice = productPrice,
            productQuantity = productQuantity,
            productImage = productImage,
            productType = productType,
            tempQuantityInCart = tempQuantityInCart
        )
        insertItem(newItem)
    }

    private fun insertItem(menuData: MenuData) {
        viewModelScope.launch {
            menuDao.insert(menuData)
        }
    }

    // Lưu trữ trạng thái của ImageView
    fun saveInstanceState(outState: Bundle) {
        outState.putParcelable(KEY_IMAGE_URI, _imageUri)
    }

    // Khôi phục trạng thái của ImageView
    fun restoreInstanceState(savedInstanceState: Bundle?) {
        _imageUri = savedInstanceState?.getParcelable(KEY_IMAGE_URI)
    }

}

class MenuViewModelFactory(private val menuDao: MenuDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MenuViewModel(menuDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
