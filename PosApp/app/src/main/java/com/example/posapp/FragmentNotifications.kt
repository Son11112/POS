package com.example.posapp

import android.content.ContentValues.TAG
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.posapp.adapter.NotificationAdapter
import com.example.posapp.viewModel.NotificationViewModel
import com.example.posapp.viewModel.NotificationViewModelFactory
import com.example.posapp.data.MyRoomDatabase
import com.example.posapp.data.NotificationData
import com.example.posapp.databinding.FragmentNotificationsBinding
import kotlinx.coroutines.launch
import java.util.*

class FragmentNotifications : Fragment() {

    private lateinit var notificationViewModel: NotificationViewModel
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = NotificationViewModelFactory(
            MyRoomDatabase.getDatabase(requireContext()).notificationDao()
        )
        notificationViewModel =
            ViewModelProvider(this, factory).get(NotificationViewModel::class.java)

        // Khởi tạo adapter
        val recyclerView = binding.notificationRecycleView
        val adapter = NotificationAdapter(listOf(), this, notificationViewModel)
        recyclerView.adapter = adapter

        // Lấy ngày hiện tại
        val currentDate = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        lifecycleScope.launch {
            val notifications = notificationViewModel.getNotificationsByDate(currentDate)

            if (notifications.isEmpty()) {
                val latestNotification = notificationViewModel.getLatestNotification()
                latestNotification?.let {
                    val latestNotifications = notificationViewModel.getNotificationsByDate(it.date)
                    (recyclerView.adapter as NotificationAdapter).updateData(latestNotifications)
                }
            } else {
                (recyclerView.adapter as NotificationAdapter).updateData(notifications)
            }
        }

        binding.btnExitToHome.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentNotifications_to_fragmentHome)
        }
        binding.btnToAddNotification.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentNotifications_to_fragmentAddNotification)
        }

        binding.btnSearch.setOnClickListener {
            val selectedDate = "${binding.datePicker.year}-${binding.datePicker.month + 1}-${binding.datePicker.dayOfMonth}"

            lifecycleScope.launch {
                val notifications = notificationViewModel.getNotificationsByDate(selectedDate)
                (recyclerView.adapter as NotificationAdapter).updateData(notifications)
            }
        }
    }
}

