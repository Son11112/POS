package com.example.posapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.posapp.dao.ShiftsDao
import com.example.posapp.data.ShiftsData
import kotlinx.coroutines.launch

class ShiftsViewModel(private val shiftsDao: ShiftsDao) : ViewModel() {

    private val allShifts: LiveData<List<ShiftsData>> = shiftsDao.getAllShifts()

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

    fun isEntryValid(employeeName: String, dateShifts: String, timeShifts: String): Boolean {
        if (employeeName.isBlank() || dateShifts.isBlank() || timeShifts.isBlank()) {
            return false
        }
        return true
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

