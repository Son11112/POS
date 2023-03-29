package com.example.posapp.viewModel

import androidx.lifecycle.*
import com.example.posapp.dao.UserDao
import com.example.posapp.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val userDao: UserDao) : ViewModel() {

    private val allUser: LiveData<List<UserData>> = userDao.getAllUser()

    fun getAllUser(): LiveData<List<UserData>> {
        return userDao.getAllUser()
    }

    fun addNewItem(
        role: String, employeeName: String, employeeCode: String,
        password: String,birth: String,phone: String,
    ) {
        val newItem = UserData(
            role = role,
            employeeName = employeeName,
            employeeCode = employeeCode,
            password = password,
            birth = birth,
            phone = phone
        )
        insertItem(newItem)
    }

    fun insertItem(userData: UserData) {
        viewModelScope.launch {
            userDao.insert(userData)
        }
    }

    suspend fun deleteEmployee(id: Int) {
        withContext(Dispatchers.IO) {
            userDao.deleteEmployee(id)
        }
    }


    fun checkEmployeeCodeExist(employeeCode: String): LiveData<Boolean> {
        val resultLiveData = MutableLiveData<Boolean>()

        viewModelScope.launch(Dispatchers.IO) {
            val count = userDao.checkEmployeeCodeExist(employeeCode)
            resultLiveData.postValue(count > 0)
        }
        return resultLiveData
    }

    fun getStaffList(): LiveData<List<UserData>> {
        return userDao.getStaffList()
    }

    suspend fun update(
        id: Int,
        role: String,
        employeeName: String,
        employeeCode: String,
        password: String,
        birth: String,phone: String,
    ) {
        withContext(Dispatchers.IO) {
            val userData = UserData(
                id = id,
                role = role,
                employeeName = employeeName,
                employeeCode = employeeCode,
                password = password,
                birth = birth,
                phone = phone
            )
            userDao.update(userData)
        }
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
