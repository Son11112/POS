package com.example.posapp.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.posapp.adapter.OrderAdapter.OrderDataViewHolder
import com.example.posapp.data.MenuData
import com.example.posapp.databinding.OrderItemBinding
import com.example.posapp.viewModel.MenuViewModel

class OrderAdapter(
    private val context: Context,
    var dataset: List<MenuData>,
    private val menuViewModel: MenuViewModel
) : RecyclerView.Adapter<OrderDataViewHolder>() {

    private val tempQuantityMap = mutableMapOf<Int, Int>()

    inner class OrderDataViewHolder(val binding: OrderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val priceTextView: TextView = binding.tvPrice
        private val quantityTextView: TextView = binding.tvQuantityOfStock
        private val nameTextView: TextView = binding.tvNameItem
        private val imageView: ImageView = binding.imgItem
        private val quantityOfCartTextView: TextView = binding.tvQuantityOfCart

        fun bind(menuData: MenuData) {
            priceTextView.text = menuData.productPrice.toString()
            quantityTextView.text = menuData.productQuantity.toString()
            nameTextView.text = menuData.productName
            if (menuData.productImage != null) {
                Glide.with(context)
                    .asBitmap()
                    .load(menuData.productImage)
                    .into(imageView)
            } else {
                imageView.setImageBitmap(null)
            }

            // Cập nhật số lượng giỏ hàng từ tempQuantityMap
            quantityOfCartTextView.text = tempQuantityMap[menuData.id]?.toString() ?: "0"

            binding.btnIncreaseQuantity.setOnClickListener {
                if (quantityOfCartTextView.text.toString().toInt() < quantityTextView.text.toString().toInt()) {
                    val newQuantity = quantityOfCartTextView.text.toString().toInt() + 1
                    tempQuantityMap[menuData.id] = newQuantity
                    quantityOfCartTextView.text = newQuantity.toString()
                }
            }

            binding.btnDecreaseQuantity.setOnClickListener {
                val currentQuantity = quantityOfCartTextView.text.toString().toInt()
                if (currentQuantity > 0) {
                    val newQuantity = currentQuantity - 1
                    tempQuantityMap[menuData.id] = newQuantity
                    quantityOfCartTextView.text = newQuantity.toString()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDataViewHolder {
        val binding = OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDataViewHolder, position: Int) {
        val item = dataset[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun setData(newData: List<MenuData>) {
        dataset = newData
        notifyDataSetChanged()
    }

    fun getItem(position: Int) = dataset[position]

    fun getQuantityAt(foodItemId: Int): Int {
        return tempQuantityMap[foodItemId] ?: 0
    }

    fun getSelectedItems(): List<Pair<MenuData, Int>> {
        return dataset.filter { tempQuantityMap[it.id] != null && tempQuantityMap[it.id]!! > 0 }
            .map { it to tempQuantityMap[it.id]!! }
    }
}
