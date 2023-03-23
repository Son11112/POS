package com.example.posapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.posapp.data.MenuData
import com.example.posapp.databinding.RecycleviewMenuBinding
import com.example.posapp.viewModel.MenuViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuAdapter(
    private val context: Context,
    var dataset: List<MenuData>,
    private var menuViewModel: MenuViewModel
) : RecyclerView.Adapter<MenuAdapter.MenuDataViewHolder>() {

    inner class MenuDataViewHolder(val binding: RecycleviewMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val kindsTextView: TextView = binding.tvKinds
        private val priceTextView: TextView = binding.tvPriceMenu
        private val quantityTextView: TextView = binding.tvQuantity
        private val nameTextView: TextView = binding.tvNameMenu
        private val imageView: ImageView = binding.imgImageMenu

        fun bind(menuData: MenuData) {
            kindsTextView.text = menuData.productKinds
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
            binding.btnUpdateMenu.setOnClickListener {
                val newPrice = binding.tvPriceMenu.text.toString().toInt()
                val newQuantity = binding.tvQuantity.text.toString().toInt()
                val newName = binding.tvNameMenu.text.toString()
                CoroutineScope(Dispatchers.Main).launch {
                    menuViewModel.updateMenu(
                        dataset[position].id,
                        menuData.productKinds,
                        menuData.productType,
                        newName,
                        newPrice,
                        newQuantity,
                        menuData.productImage?.copyOf(),
                        menuData.tempQuantityInCart
                    )
                }
                Toast.makeText(context, "メニュー更新しました！", Toast.LENGTH_SHORT).show()
            }

            binding.btnDelete.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    menuViewModel.deleteMenu(dataset[position].id)
                }
                Toast.makeText(context, "商品削除しました！", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapter.MenuDataViewHolder {
        val binding = RecycleviewMenuBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MenuDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuAdapter.MenuDataViewHolder, position: Int) {
        val item = dataset[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun getItem(position: Int) = dataset[position]
}



