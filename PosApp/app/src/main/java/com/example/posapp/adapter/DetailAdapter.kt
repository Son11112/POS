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
    private var menuDataList: List<MenuData>,
    private var orderViewModel: OrderViewModel
): RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {

    private var orderFoodItems: List<OrderFoodItem> = listOf()
    inner class DetailViewHolder(val binding: RecycleviewOrderDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val nameDetailTextView = binding.tvNameMenu
        private val priceDetailTextView = binding.tvPriceMenu
        private val quantityDetailTextView = binding.tvQuantityInOrder
//        private val quantityInStockTextView = binding.tvQuantityInStock

        fun bind(menuData: MenuData, orderFoodItem: OrderFoodItem) {
            nameDetailTextView.text = menuData.productName
            priceDetailTextView.text = menuData.productPrice.toString()
            quantityDetailTextView.text = orderFoodItem.quantityInCart.toString()
//            quantityInStockTextView.text = menuData.productQuantity.toString()

//            binding.btnPlus.setOnClickListener {
//                if (quantityDetailTextView.text.toString().toInt() < quantityInStockTextView.text.toString().toInt()) {
//                    val newQuantity = quantityDetailTextView.text.toString().toInt() + 1
//                    binding.tvQuantityInOrder.text = newQuantity.toString()
//                }
//            }
//
//            binding.btnMinus.setOnClickListener {
//                if (quantityDetailTextView.text.toString().toInt()>0){
//                    val newQuantity = quantityDetailTextView.text.toString().toInt() - 1
//                    binding.tvQuantityInOrder.text = newQuantity.toString()
//                }
//            }
//            binding.btnUpdateMenu.setOnClickListener {
//                var newQuantity = binding.tvQuantityInOrder.text.toString().toInt()
//                CoroutineScope(Dispatchers.Main).launch {
//                    orderViewModel.updateQuantityInCart(
//                        menuDataList[position].id,
//                        newQuantity
//                    )
//                }
//            }
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