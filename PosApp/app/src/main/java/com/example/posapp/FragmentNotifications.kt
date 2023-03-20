package com.example.posapp

import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.posapp.adapter.NotificationAdapter
import com.example.posapp.viewModel.NotificationViewModel
import com.example.posapp.viewModel.NotificationViewModelFactory
import com.example.posapp.data.MyRoomDatabase
import com.example.posapp.databinding.FragmentNotificationsBinding

class FragmentNotifications : Fragment() {

    private lateinit var notificationAdapter: NotificationAdapter
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = NotificationViewModelFactory(
            MyRoomDatabase.getDatabase(requireContext()).notificationDao()
        )
        notificationViewModel =
            ViewModelProvider(this, factory).get(NotificationViewModel::class.java)
        // Khởi tạo adapter

        val recyclerView = view.findViewById<RecyclerView>(R.id.NotificationRecycleView)

        // Lấy dữ liệu từ ViewModel và cập nhật lên RecyclerView
        notificationViewModel.getNotification().observe(viewLifecycleOwner) { notification ->
            Log.d(TAG, "Notification $notification")
            var adapter = NotificationAdapter(requireContext(), notification)
            recyclerView.adapter = adapter
        }

        binding.btnExitToHome.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentNotifications_to_fragmentHome)
        }
        binding.btnToAddNotification.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentNotifications_to_fragmentAddNotification)
        }
    }
}

