package com.example.posapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.posapp.data.MenuData
import com.example.posapp.databinding.RecyclerviewCartBinding

class CartAdapter(
    private val context: Context,
    var dataset: List<MenuData>,
    private val listener: CartAdapterListener?,
) : RecyclerView.Adapter<CartAdapter.CartDataViewHolder>() {

    inner class CartDataViewHolder(val binding: RecyclerviewCartBinding) : RecyclerView.ViewHolder(binding.root) {
        private val priceCartTextView: TextView = binding.tvPrice
        private val quantityStockTextView: TextView = binding.tvQuantityOfStock
        private val nameCartTextView: TextView = binding.tvNameItem
        private val imageCartView: ImageView = binding.imgItem
        private val quantityOfCartTextView: TextView = binding.tvQuantityOfCart

        fun bind(menuData: MenuData) {
            priceCartTextView.text = menuData.productPrice.toString()
            quantityStockTextView.text = menuData.productQuantity.toString()
            nameCartTextView.text = menuData.productName
            quantityOfCartTextView.text = menuData.tempQuantityInCart.toString()
            if (menuData.productImage != null) {
                Glide.with(context)
                    .asBitmap()
                    .load(menuData.productImage)
                    .into(imageCartView)
            }else{
                imageCartView.setImageBitmap(null)
            }
            binding.btnIncreaseQuantity.setOnClickListener {
                if (quantityOfCartTextView.text.toString().toInt() < quantityStockTextView.text.toString().toInt()){
                    menuData.tempQuantityInCart++
                    binding.tvQuantityOfCart.text = menuData.tempQuantityInCart.toString()
                    listener?.onTotalPriceChanged(getTotalPrice())
                }
            }
            binding.btnDecreaseQuantity.setOnClickListener {
                if (menuData.tempQuantityInCart >0 ){
                    menuData.tempQuantityInCart--
                    binding.tvQuantityOfCart.text = menuData.tempQuantityInCart.toString()
                    listener?.onTotalPriceChanged(getTotalPrice())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartDataViewHolder {
        val binding = RecyclerviewCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartDataViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun getTotalPrice(): Int {
        var totalPrice = 0
        for (order in dataset) {
            totalPrice += order.productPrice*order.tempQuantityInCart
        }
        return totalPrice
    }

    fun getItem(position: Int) = dataset[position]
}

     interface CartAdapterListener {
          fun onTotalPriceChanged(totalPrice: Int)
}


