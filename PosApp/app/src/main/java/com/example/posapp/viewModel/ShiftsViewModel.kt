package com.example.posapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.posapp.dao.ShiftsDao
import com.example.posapp.data.MenuData
import com.example.posapp.data.NotificationData
import com.example.posapp.data.ShiftsData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShiftsViewModel(private val shiftsDao: ShiftsDao) : ViewModel() {

    fun getAllShifts(): LiveData<List<ShiftsData>> {
        return shiftsDao.getAllShifts()
    }

    fun addNewItem(employeeName: String, dateShifts: String, timeShifts: String) {
        val newItem = ShiftsData(
            employeeName = employeeName,
            dateShifts = dateShifts,
            timeShifts = timeShifts
        )
        insertItem(newItem)
    }

    private fun insertItem(shiftsData: ShiftsData) {
        viewModelScope.launch {
            shiftsDao.insert(shiftsData)
        }
    }

    suspend fun deleteShift(id: Int) {
        withContext(Dispatchers.IO) {
            shiftsDao.deleteShift(id)
        }
    }

    fun isEntryValid(employeeName: String, dateShifts: String, timeShifts: String): Boolean {
        if (employeeName.isBlank() || dateShifts.isBlank() || timeShifts.isBlank()) {
            return false
        }
        return true
    }

    suspend fun delete(id: Int) {
        withContext(Dispatchers.IO){
            shiftsDao.deleteShift(id)
        }
    }

    suspend fun update(
        id: Int,
        employeeName: String,
        dateShifts: String,
        timeShifts: String
    ) {
        withContext(Dispatchers.IO) {
            val shiftsData = ShiftsData(
                id = id,
                employeeName = employeeName,
                dateShifts = dateShifts,
                timeShifts = timeShifts
            )
            shiftsDao.update(shiftsData)
        }
    }

    fun getShiftByDate(selectedDate: String): LiveData<List<ShiftsData>> {
        return shiftsDao.getShiftsByDate(selectedDate)
    }
}

class ShiftsViewModelFactory(private val shiftsDao: ShiftsDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShiftsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShiftsViewModel(shiftsDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

