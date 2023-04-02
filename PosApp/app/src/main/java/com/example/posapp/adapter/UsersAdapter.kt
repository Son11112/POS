package com.example.posapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.posapp.databinding.RecycleviewUsersBinding
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.posapp.FragmentUsers
import com.example.posapp.PasswordUtils.PasswordUtils
import com.example.posapp.data.UserData
import com.example.posapp.viewModel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsersAdapter(
    val context: Context,
    var dataset: List<UserData>,
    private var userViewModel: UserViewModel,
    private var fragmentUsers: FragmentUsers
) : RecyclerView.Adapter<UsersAdapter.UserDataViewHolder>() {

    inner class UserDataViewHolder(val binding: RecycleviewUsersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val employeeNameTextView: TextView = binding.tvName
        private val employeeCodeTextView: TextView = binding.tvCode
        private val roleTextView: TextView = binding.tvRole
        private val passTextView: TextView = binding.tvPass
        private val birthTextView: TextView = binding.tvBirth
        private val phoneTextView: TextView = binding.tvPhone

        fun bind(userData: UserData) {
            employeeNameTextView.text = userData.employeeName
            employeeCodeTextView.text = userData.employeeCode
            roleTextView.text = userData.role
            birthTextView.text = userData.birth
            phoneTextView.text = userData.phone

            if (userData.role == "admin")
            binding.btnPass.visibility = View.VISIBLE

            binding.btnPass.setOnClickListener {
                if (passTextView.visibility == View.INVISIBLE) {
                    passTextView.visibility = View.VISIBLE
                } else {
                    passTextView.visibility = View.INVISIBLE
                }
            }

            binding.btnDelete.setOnClickListener {
                showDeleteConfirmationDialog(dataset[position].id)
            }

            binding.btnUpdate.setOnClickListener {
                val newName = binding.tvName.text.toString()
                val newPhone = binding.tvPhone.text.toString()
                val hashedPassword = PasswordUtils.hashPassword(binding.tvPass.text.toString())
                AlertDialog.Builder(fragmentUsers.requireContext())
                    .setTitle("確認")
                    .setMessage("更新しますか？")
                    .setPositiveButton("はい") { dialog, _ ->
                        // Xác nhận xóa dữ liệu
                        CoroutineScope(Dispatchers.Main).launch {
                            userViewModel.update(
                                dataset[position].id,
                                userData.role,
                                newName,
                                userData.employeeCode,
                                hashedPassword,
                                userData.birth,
                                newPhone,
                            )
                        }
                        Toast.makeText(fragmentUsers.context, "更新しました！", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .setNegativeButton("いいえ") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UsersAdapter.UserDataViewHolder {
        val binding =
            RecycleviewUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    private fun showDeleteConfirmationDialog(Id: Int) {
        AlertDialog.Builder(fragmentUsers.requireContext())
            .setTitle("確認")
            .setMessage("本当に削除しますか？")
            .setPositiveButton("はい") { dialog, _ ->
                // Xác nhận xóa dữ liệu
                CoroutineScope(Dispatchers.Main).launch {
                    userViewModel.deleteEmployee(Id)
                }
                Toast.makeText(fragmentUsers.context, "削除しました！", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("いいえ") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}