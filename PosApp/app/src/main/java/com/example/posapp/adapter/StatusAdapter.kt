package com.example.posapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.posapp.data.MenuData
import com.example.posapp.data.OrdersData
import com.example.posapp.databinding.RecyclerviewStatusBinding
import com.example.posapp.viewModel.OrderViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StatusAdapter(
    private val context: Context,
    var dataset : List<OrdersData>,
    private var orderViewModel: OrderViewModel
): RecyclerView.Adapter<StatusAdapter.StatusDataViewHolder>() {

    inner class StatusDataViewHolder(val binding: RecyclerviewStatusBinding) : RecyclerView.ViewHolder(binding.root){
        private val orderIdTextView: TextView = binding.tvOrderId
        private val orderDateTextView: TextView = binding.tvOrderDate
        private val orderTimeTextView: TextView = binding.tvOrderTime
        private val totalPriceTextView: TextView = binding.tvTotalPrice
        private val orderStatusTextView: TextView = binding.tvOrderStatus

        fun bind(ordersData: OrdersData){
            orderIdTextView.text= dataset[position].id.toString()
            orderDateTextView.text= ordersData.orderDate
            orderTimeTextView.text= ordersData.orderTime
            totalPriceTextView.text = ordersData.totalPrice.toString()+"円"
            orderStatusTextView.text= ordersData.orderStatus
            binding.btnStatusChange.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val newStatus = "offered"
                    orderViewModel.upDateStatus(dataset[position].id, newStatus)
                }
            }
            binding.btnDelete.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val newStatus = "canceled"
                    orderViewModel.cancelOrder(dataset[position].id, newStatus)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusAdapter.StatusDataViewHolder {
        val binding = RecyclerviewStatusBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return StatusDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatusDataViewHolder, position: Int) {
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
}