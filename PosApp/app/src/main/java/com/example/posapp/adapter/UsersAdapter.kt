package com.example.posapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.posapp.R
import com.example.posapp.data.UserData

class UsersAdapter(
    val context: Context,
    var dataset: List<UserData>
) : RecyclerView.Adapter<UsersAdapter.UserDataViewHolder>() {

    class UserDataViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val employeeNameTextView: TextView = view.findViewById(R.id.tvName)
        val employeeCodeTextView: TextView = view.findViewById(R.id.tvCode)
        val employeePassTextView: TextView = view.findViewById(R.id.tvPass)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDataViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_users, parent, false)
        return UserDataViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: UsersAdapter.UserDataViewHolder, position: Int) {
        val item = dataset[position]
        holder.employeeNameTextView.text = item.employeeName
        holder.employeeCodeTextView.text = item.employeeCode
        holder.employeePassTextView.text = item.password
    }

    override fun getItemCount() = dataset.size
}