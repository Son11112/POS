package com.example.posapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.posapp.data.MenuData
import com.example.posapp.data.OrderFoodItem
import com.example.posapp.databinding.RecyclerviewCartBinding
import com.example.posapp.databinding.RecycleviewPayBinding

class PayAdapter(
    var menuDataList: List<MenuData>
) : RecyclerView.Adapter<PayAdapter.PayDataViewHolder>() {

    private var orderFoodItems: List<OrderFoodItem> = listOf()

    inner class PayDataViewHolder(val binding: RecycleviewPayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val productNamePayTextView: TextView = binding.tvProductNamePay
        private val productPricePayTextView: TextView = binding.tvProductPricePay
        private val quantityOfCartPay: TextView = binding.tvQuantityOfCartPay
        private val pricePay: TextView = binding.tvPricePay

        fun bind(menuData: MenuData, orderFoodItem: OrderFoodItem) {
            productNamePayTextView.text = menuData.productName
            productPricePayTextView.text = menuData.productPrice.toString()
            quantityOfCartPay.text = orderFoodItem.quantityInCart.toString()
            pricePay.text = (menuData.productPrice * orderFoodItem.quantityInCart).toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayDataViewHolder {
        val binding =
            RecycleviewPayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PayDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PayAdapter.PayDataViewHolder, position: Int) {
        val orderFoodItem = orderFoodItems[position]
        val menuData = menuDataList.firstOrNull { it.id == orderFoodItem.foodItemId }
        if (menuData != null) {
            holder.bind(menuData, orderFoodItem)
        }
    }

    override fun getItemCount(): Int {
        return menuDataList.size
    }

    fun submitList(newList: List<MenuData>) {
        menuDataList = newList
        notifyDataSetChanged()
    }
}


