package com.example.posapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.posapp.adapter.OrderAdapter.OrderDataViewHolder
import com.example.posapp.data.MenuData
import com.example.posapp.data.OrdersData
import com.example.posapp.databinding.OrderItemBinding
import com.example.posapp.viewModel.MenuViewModel

class OrderAdapter(
    private val context: Context,
    var dataset: List<MenuData>,
    private val menuViewModel: MenuViewModel
) : RecyclerView.Adapter<OrderDataViewHolder>() {

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
            quantityOfCartTextView.text = menuData.tempQuantityInCart.toString()
            if (menuData.productImage != null) {
                Glide.with(context)
                    .asBitmap()
                    .load(menuData.productImage)
                    .into(imageView)
            } else {
                imageView.setImageBitmap(null)
            }
            binding.btnIncreaseQuantity.setOnClickListener {
                if (quantityOfCartTextView.text.toString().toInt() < quantityTextView.text.toString().toInt()) {
                    menuData.tempQuantityInCart++
                    binding.tvQuantityOfCart.text = menuData.tempQuantityInCart.toString()
                }
            }
            binding.btnDecreaseQuantity.setOnClickListener {
                if(menuData.tempQuantityInCart >0 ){
                    menuData.tempQuantityInCart --
                    binding.tvQuantityOfCart.text = menuData.tempQuantityInCart.toString()
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
}
