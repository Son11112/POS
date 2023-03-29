package com.example.posapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.posapp.FragmentShifts
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
    private var shiftsViewModel: ShiftsViewModel,
    private var fragmentShifts: FragmentShifts
) : RecyclerView.Adapter<ShiftsAdapter.ShiftsDataViewHolder>() {

    inner class ShiftsDataViewHolder(val binding: RecycleviewShiftsBinding) : RecyclerView.ViewHolder(binding.root) {
        private val employeeNameTextView: TextView = binding.tvName
        private val dateShiftsTextView: TextView = binding.tvDate
        private val timeShiftsTextView: TextView = binding.tvTime

        fun bind(shiftsData: ShiftsData) {
            employeeNameTextView.text = shiftsData.employeeName
            dateShiftsTextView.text = shiftsData.dateShifts
            timeShiftsTextView.text = shiftsData.timeShifts
            binding.btnDelete.setOnClickListener {
                showDeleteConfirmationDialog(dataset[position].id)
            }
            binding.btnUpdate.setOnClickListener {
                val newName = binding.tvName.text.toString()
                val newDate = binding.tvDate.text.toString()
                val newTime = binding.tvTime.text.toString()
                AlertDialog.Builder(fragmentShifts.requireContext())
                    .setTitle("確認")
                    .setMessage("更新しますか？")
                    .setPositiveButton("はい") { dialog, _ ->
                        CoroutineScope(Dispatchers.Main).launch {
                            shiftsViewModel.update(
                                dataset[position].id,
                                newName,
                                newDate,
                                newTime
                            )
                        }
                        Toast.makeText(fragmentShifts.context, "更新しました！", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .setNegativeButton("いいえ") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
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

    private fun showDeleteConfirmationDialog(Id: Int) {
        AlertDialog.Builder(fragmentShifts.requireContext())
            .setTitle("確認")
            .setMessage("本当に削除しますか？")
            .setPositiveButton("はい") { dialog, _ ->
                // Xác nhận xóa dữ liệu
                CoroutineScope(Dispatchers.Main).launch {
                    shiftsViewModel.delete(Id)
                }
                Toast.makeText(fragmentShifts.context, "削除しました！", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("いいえ") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
