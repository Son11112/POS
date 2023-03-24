package com.example.posapp.adapter

import android.content.Context
import android.view.LayoutInflater
import com.example.posapp.databinding.RecycleviewUsersBinding
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.posapp.R
import com.example.posapp.data.UserData
import com.example.posapp.viewModel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsersAdapter(
    val context: Context,
    var dataset: List<UserData>,
    private var userViewModel: UserViewModel
) : RecyclerView.Adapter<UsersAdapter.UserDataViewHolder>() {

    inner class UserDataViewHolder(val binding : RecycleviewUsersBinding) : RecyclerView.ViewHolder(binding.root) {
        val employeeNameTextView: TextView = binding.tvName
        val employeeCodeTextView: TextView = binding.tvCode
        val employeePassTextView: TextView = binding.tvPass

        fun bind(userData: UserData) {
            employeeNameTextView.text = userData.employeeName
            employeeCodeTextView.text = userData.employeeCode
            employeePassTextView.text = userData.password
            binding.btnDelete.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    userViewModel.deleteEmployee(dataset[position].id)
                }
                Toast.makeText(context, "削除しました！", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapter.UserDataViewHolder {
        val binding = RecycleviewUsersBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return UserDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersAdapter.UserDataViewHolder, position: Int) {
        val item = dataset[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun getItem(position: Int) = dataset[position]
}