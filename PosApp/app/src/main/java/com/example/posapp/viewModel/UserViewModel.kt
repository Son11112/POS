package com.example.posapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.posapp.dao.UserDao
import com.example.posapp.data.UserData
import kotlinx.coroutines.launch

class UserViewModel(private val userDao: UserDao) : ViewModel() {

    private val allUser: LiveData<List<UserData>> = userDao.getAllUser()

    fun getAllUser(): LiveData<List<UserData>> {
        return userDao.getAllUser()
    }

    fun addNewItem(
        role: String, employeeName: String, employeeCode: String,
        password: String
    ) {
        val newItem = UserData(
            role = role,
            employeeName = employeeName,
            employeeCode = employeeCode,
            password = password
        )
        insertItem(newItem)
    }

    fun insertItem(userData: UserData) {
        viewModelScope.launch {
            userDao.insert(userData)
        }
    }

    suspend fun getRoleByEmployeeCode(employeeCode: String): UserData {
        return userDao.getUserByEmployeeCode(employeeCode)
    }
}


class UserViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
