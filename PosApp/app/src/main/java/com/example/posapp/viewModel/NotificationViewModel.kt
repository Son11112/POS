package com.example.posapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.posapp.dao.NotificationDao
import com.example.posapp.data.NotificationData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationViewModel(private val notificationDao: NotificationDao) : ViewModel() {

    suspend fun delete(id: Int) {
        withContext(Dispatchers.IO) {
            notificationDao.delete(id)
        }
    }

    suspend fun update(
        id: Int,
        date: String,
        subject: String,
        detailed : String
    ) {
        withContext(Dispatchers.IO) {
            val notificationData = NotificationData(
                id = id,
                date = date,
                subject = subject,
                detailed = detailed
            )
            notificationDao.update(notificationData)
        }
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

    suspend fun getNotificationsByDate(selectedDate: String): List<NotificationData> {
        return notificationDao.getNotificationsByDate(selectedDate)
    }

    suspend fun getLatestNotification(): NotificationData? {
        return notificationDao.getLatestNotification()
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

