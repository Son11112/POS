package com.example.posapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.posapp.dao.NotificationDao
import com.example.posapp.data.NotificationData
import kotlinx.coroutines.launch

class NotificationViewModel(private val notificationDao: NotificationDao) : ViewModel() {

    private val allNotification: LiveData<List<NotificationData>> =
        notificationDao.getNotification()

    fun getNotification(): LiveData<List<NotificationData>> {
        return notificationDao.getNotification()
    }

    fun addNewItem(date: String, subject: String, detailed: String) {
        val newItem = NotificationData(
            date = date,
            subject = subject,
            detailed = detailed

        )
        insertItem(newItem)
    }

    fun insertItem(notificationData: NotificationData) {
        viewModelScope.launch {
            notificationDao.insert(notificationData)
        }
    }
}

class NotificationViewModelFactory(private val notificationDao: NotificationDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationViewModel(notificationDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

