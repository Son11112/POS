package com.example.posapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.posapp.FragmentStatus
import com.example.posapp.data.MenuData
import com.example.posapp.data.OrdersData
import com.example.posapp.databinding.RecyclerviewStatusBinding
import com.example.posapp.viewModel.OrderViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StatusAdapter(
    var dataset : List<OrdersData>,
    private var orderViewModel: OrderViewModel,
    private val onDetailButtonClickListener:OnDetailButtonClickListener,
    private val fragmentStatus: FragmentStatus
): RecyclerView.Adapter<StatusAdapter.StatusDataViewHolder>() {

    inner class StatusDataViewHolder(val binding: RecyclerviewStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val orderIdTextView: TextView = binding.tvOrderId
        private val orderDateTextView: TextView = binding.tvOrderDate
        private val orderTimeTextView: TextView = binding.tvOrderTime
        private val totalPriceTextView: TextView = binding.tvTotalPrice
        private val orderStatusTextView: TextView = binding.tvOrderStatus
        private val payTextView: TextView = binding.tvPayMethod

        fun bind(ordersData: OrdersData) {
            orderIdTextView.text = ordersData.tableNumber.toString()
            orderDateTextView.text = ordersData.orderDate
            orderTimeTextView.text = ordersData.orderTime
            totalPriceTextView.text = ordersData.totalPrice.toString() + "円"
            orderStatusTextView.text = ordersData.orderStatus
            payTextView.text = ordersData.payMethod

            binding.btnStatusChange.setOnClickListener {
                binding.btnStatusChange.visibility = View.INVISIBLE
                binding.btnDelete.visibility = View.INVISIBLE
                AlertDialog.Builder(fragmentStatus.requireContext())
                    .setTitle("確認")
                    .setMessage("更新しますか？")
                    .setPositiveButton("はい") { dialog, _ ->
                CoroutineScope(Dispatchers.Main).launch {
                    val newStatus = "offered"
                    orderViewModel.updateStatus(dataset[position].id, newStatus)
                }
                        Toast.makeText(fragmentStatus.context, "更新しました！", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .setNegativeButton("いいえ") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }


            binding.btnDelete.setOnClickListener {
                binding.btnStatusChange.visibility = View.INVISIBLE
                binding.btnDelete.visibility = View.INVISIBLE

                AlertDialog.Builder(fragmentStatus.requireContext())
                    .setTitle("確認")
                    .setMessage("更新しますか？")
                    .setPositiveButton("はい") { dialog, _ ->
                        CoroutineScope(Dispatchers.Main).launch {
                            val newStatus = "canceled"
                            orderViewModel.cancelOrder(dataset[position].id, newStatus)
                        }
                        Toast.makeText(fragmentStatus.context, "キャンセルしました！", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .setNegativeButton("いいえ") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }


            binding.btnDetail.setOnClickListener {
                onDetailButtonClickListener.onDetailButtonClick(ordersData.orderId)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StatusAdapter.StatusDataViewHolder {
        val binding =
            RecyclerviewStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatusDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatusAdapter.StatusDataViewHolder, position: Int) {
        val item = dataset[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun getItem(position: Int) = dataset[position]

    fun setData(newData: List<OrdersData>) {
        dataset = newData
        notifyDataSetChanged()
    }

    interface OnDetailButtonClickListener {
        fun onDetailButtonClick(orderId: String)
    }
}