package com.example.posapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.posapp.FragmentPay
import com.example.posapp.data.OrderFoodItem
import com.example.posapp.data.OrdersData
import com.example.posapp.databinding.RecycleviewPayBinding

class PayAdapter(
    private var dataset: List<OrdersData>,
    private val onCheckedChangeListener: OnCheckedChangeListener
) : RecyclerView.Adapter<PayAdapter.PayDataViewHolder>() {

    inner class PayDataViewHolder(val binding: RecycleviewPayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val productNamePayTextView: TextView = binding.tvTablePay
        private val productPricePayTextView: TextView = binding.tvOrderPay
        private val quantityOfCartPay: TextView = binding.tvTotalPay
        private val pricePay: TextView = binding.tvPay

        init {
            binding.btnCheckPay.setOnCheckedChangeListener { _, isChecked ->
                val item = dataset[adapterPosition]
                onCheckedChangeListener.onItemChecked(item, isChecked)
            }
        }

        fun bind(ordersData: OrdersData) {
            productNamePayTextView.text = ordersData.tableNumber.toString()
            productPricePayTextView.text = ordersData.orderStatus
            quantityOfCartPay.text = ordersData.totalPrice.toString()
            pricePay.text = ordersData.payMethod
            if (ordersData.payMethod =="unpaid"){
                binding.btnCheckPay.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayAdapter.PayDataViewHolder {
        val binding =
            RecycleviewPayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PayDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PayAdapter.PayDataViewHolder, position: Int) {
        val item = dataset[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun updateDataset(newDataset: List<OrdersData>) {
        dataset = newDataset
        notifyDataSetChanged()
    }

    interface OnCheckedChangeListener {
        fun onItemChecked(item: OrdersData, isChecked: Boolean)
    }
}
