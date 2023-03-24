package com.example.posapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.posapp.R
import com.example.posapp.data.ShiftsData
import com.example.posapp.databinding.FragmentAddNotificationBinding
import com.example.posapp.databinding.RecycleviewMenuBinding
import com.example.posapp.databinding.RecycleviewShiftsBinding
import com.example.posapp.viewModel.ShiftsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShiftsAdapter(
    private val context: Context,
    private var dataset: List<ShiftsData>,
    private var shiftsViewModel: ShiftsViewModel
) : RecyclerView.Adapter<ShiftsAdapter.ShiftsDataViewHolder>() {

    inner class ShiftsDataViewHolder(val binding: RecycleviewShiftsBinding) : RecyclerView.ViewHolder(binding.root) {
        private val employeeNameTextView: TextView = binding.tvName
        private val dateShiftsTextView: TextView = binding.tvTime
        private val timeShiftsTextView: TextView = binding.tvDate

        fun bind(shiftsData: ShiftsData){
            employeeNameTextView.text = shiftsData.employeeName
            dateShiftsTextView.text = shiftsData.dateShifts
            timeShiftsTextView.text = shiftsData.timeShifts
            binding.btnDelete.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    shiftsViewModel.deleteShift(dataset[position].id)
                }
                Toast.makeText(context, "削除しました！", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShiftsDataViewHolder {
        val binding = RecycleviewShiftsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShiftsDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShiftsDataViewHolder, position: Int) {
        val item = dataset[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun getItem(position: Int) = dataset[position]
}
