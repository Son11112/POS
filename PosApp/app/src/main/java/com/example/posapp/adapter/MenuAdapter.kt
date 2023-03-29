package com.example.posapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.posapp.FragmentMenu
import com.example.posapp.data.MenuData
import com.example.posapp.databinding.RecycleviewMenuBinding
import com.example.posapp.viewModel.MenuViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuAdapter(
    private val context: Context,
    var dataset: List<MenuData>,
    private var menuViewModel: MenuViewModel,
    private var fragmentMenu: FragmentMenu
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
                AlertDialog.Builder(fragmentMenu.requireContext())
                    .setTitle("確認")
                    .setMessage("更新しますか？")
                    .setPositiveButton("はい") { dialog, _ ->
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
                        Toast.makeText(fragmentMenu.context, "更新しました！", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .setNegativeButton("いいえ") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }

            binding.btnDelete.setOnClickListener {
                showDeleteConfirmationDialog(dataset[position].id)
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

    private fun showDeleteConfirmationDialog(Id: Int) {
        AlertDialog.Builder(fragmentMenu.requireContext())
            .setTitle("確認")
            .setMessage("本当に削除しますか？")
            .setPositiveButton("はい") { dialog, _ ->
                // Xác nhận xóa dữ liệu
                CoroutineScope(Dispatchers.Main).launch {
                    menuViewModel.deleteMenu(Id)
                }
                Toast.makeText(fragmentMenu.context, "削除しました！", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("いいえ") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}



