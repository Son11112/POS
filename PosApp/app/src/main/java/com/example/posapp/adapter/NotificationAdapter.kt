package com.example.posapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.posapp.FragmentNotifications
import com.example.posapp.R
import com.example.posapp.data.NotificationData
import com.example.posapp.databinding.FragmentAddNotificationBinding
import com.example.posapp.databinding.RecycleviewNotificationsBinding
import com.example.posapp.viewModel.NotificationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationAdapter (
    private var dataset: List<NotificationData>,
    private var fragmentNotifications: FragmentNotifications,
    private var notificationViewModel: NotificationViewModel
) :RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>(){

   inner class NotificationViewHolder(val binding: RecycleviewNotificationsBinding) :RecyclerView.ViewHolder(binding.root){
        private val dateTextView :TextView = binding.tvDate
        private val subjectTextView :TextView = binding.tvSubject
        private val detailedTextView :TextView = binding.tvDetailed

       fun bind(notificationData: NotificationData) {
           dateTextView.text = notificationData.date
           subjectTextView.text = notificationData.subject
           detailedTextView.text = notificationData.detailed

           binding.btnDelete.setOnClickListener {
               showDeleteConfirmationDialog(dataset[position].id)
           }

           binding.btnUpdate.setOnClickListener {
               val newSubject = binding.tvSubject.text.toString()
               val newDetailed = binding.tvDetailed.text.toString()
               AlertDialog.Builder(fragmentNotifications.requireContext())
                   .setTitle("確認")
                   .setMessage("更新しますか？")
                   .setPositiveButton("はい") { dialog, _ ->
                       CoroutineScope(Dispatchers.Main).launch {
                           notificationViewModel.update(
                               dataset[position].id,
                               notificationData.date,
                               newSubject,
                               newDetailed
                           )
                       }
                       Toast.makeText(fragmentNotifications.context, "更新しました！", Toast.LENGTH_SHORT)
                           .show()
                       dialog.dismiss()
                   }
                   .setNegativeButton("いいえ") { dialog, _ ->
                       dialog.dismiss()
                   }
                   .show()
           }
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.NotificationViewHolder {
        val binding = RecycleviewNotificationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = dataset[position]
        holder.bind(item)
    }

    fun updateData(newNotificationList: List<NotificationData>) {
        dataset = newNotificationList
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataset.size

    private fun showDeleteConfirmationDialog(Id: Int) {
        AlertDialog.Builder(fragmentNotifications.requireContext())
            .setTitle("確認")
            .setMessage("本当に削除しますか？")
            .setPositiveButton("はい") { dialog, _ ->
                // Xác nhận xóa dữ liệu
                CoroutineScope(Dispatchers.Main).launch {
                    notificationViewModel.delete(Id)
                }
                Toast.makeText(fragmentNotifications.context, "削除しました！", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("いいえ") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}