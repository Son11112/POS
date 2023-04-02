package com.example.posapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.posapp.FragmentStatus
import com.example.posapp.data.MenuData
import com.example.posapp.data.OrderFoodItem
import com.example.posapp.databinding.RecycleviewOrderDetailBinding
import com.example.posapp.viewModel.MenuViewModel
import com.example.posapp.viewModel.OrderViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailAdapter(
    var menuDataList: List<MenuData>
): RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {

    private var orderFoodItems: List<OrderFoodItem> = listOf()
    inner class DetailViewHolder(val binding: RecycleviewOrderDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val nameDetailTextView = binding.tvNameMenu
        private val priceDetailTextView = binding.tvPriceMenu
        private val quantityDetailTextView = binding.tvQuantityInOrder

        fun bind(menuData: MenuData, orderFoodItem: OrderFoodItem) {
            nameDetailTextView.text = menuData.productName
            priceDetailTextView.text = menuData.productPrice.toString()
            quantityDetailTextView.text = orderFoodItem.quantityInCart.toString()
        }
    }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val binding = RecycleviewOrderDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val orderFoodItem = orderFoodItems[position]
        val menuData = menuDataList.firstOrNull { it.id == orderFoodItem.foodItemId }
        if (menuData != null) {
            holder.bind(menuData, orderFoodItem)
        }
    }

    override fun getItemCount(): Int {
        return orderFoodItems.size
    }

    fun submitList(newList: List<OrderFoodItem>) {
        orderFoodItems = newList
        notifyDataSetChanged()
    }
}