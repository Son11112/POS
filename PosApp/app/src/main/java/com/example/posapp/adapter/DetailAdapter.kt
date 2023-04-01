package com.example.posapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.posapp.data.MenuData
import com.example.posapp.data.OrderFoodItem
import com.example.posapp.databinding.RecycleviewOrderDetailBinding

class DetailAdapter(
    private val context: Context,
    var dataset: List<OrderFoodItem>,
    var menuDataList: List<MenuData>
): RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {

    private var orderFoodItems: List<OrderFoodItem> = listOf()

    inner class DetailViewHolder(val binding: RecycleviewOrderDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val nameDetailTextView = binding.tvNameMenu
        private val priceDetailTextView = binding.tvPriceMenu
        private val stockDetailTextView = binding.tvQuantityInStock
        private val quantityDetailTextView = binding.tvQuantityInOrder

        fun bind(menuData: MenuData, orderFoodItem: OrderFoodItem) {
            nameDetailTextView.text = menuData.productName
            priceDetailTextView.text = menuData.productPrice.toString()
            stockDetailTextView.text = menuData.productQuantity.toString()
            quantityDetailTextView.text = orderFoodItem.quantityInCart.toString()

            binding.btnPlus.setOnClickListener {
                if (quantityDetailTextView.text.toString().toInt()< stockDetailTextView.text.toString().toInt()){
                    orderFoodItem.quantityInCart++
                    binding.tvQuantityInOrder.text = orderFoodItem.quantityInCart.toString()
                }
            }
            binding.btnMinus.setOnClickListener {
                if (orderFoodItem.quantityInCart>0){
                    orderFoodItem.quantityInCart--
                    binding.tvQuantityInOrder.text = orderFoodItem.quantityInCart.toString()
                }
            }
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