package com.example.posapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.posapp.R
import com.example.posapp.data.MenuData

class MenuAdapter(
    private val context: Context,
    var dataset: List<MenuData>
) : RecyclerView.Adapter<MenuAdapter.MenuDataViewHolder>() {

    class MenuDataViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val kindsTextView: TextView = view.findViewById(R.id.tvKinds)
        val priceTextView: TextView = view.findViewById(R.id.tvPriceMenu)
        val quantityTextView: TextView = view.findViewById(R.id.tvQuantity)
        val nameTextView: TextView = view.findViewById(R.id.tvNameMenu)
        val imageView: ImageView = view.findViewById(R.id.imgImageMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuDataViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_menu, parent, false)
        return MenuDataViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: MenuDataViewHolder, position: Int) {
        val item = dataset[position]
        holder.kindsTextView.text = item.productKinds
        holder.priceTextView.text = item.productPrice.toString()
        holder.quantityTextView.text = item.productQuantity.toString()
        holder.nameTextView.text = item.productName
        if (item.productImage != null) {
            Glide.with(context)
                .asBitmap()
                .load(item.productImage)
                .into(holder.imageView)
        } else {
            holder.imageView.setImageBitmap(null)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}