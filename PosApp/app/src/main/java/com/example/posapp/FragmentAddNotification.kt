package com.example.posapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.posapp.data.MyRoomDatabase
import com.example.posapp.viewModel.NotificationViewModel
import com.example.posapp.databinding.FragmentAddNotificationBinding
import com.example.posapp.viewModel.NotificationViewModelFactory
import com.example.posapp.viewModel.UserViewModel
import com.example.posapp.viewModel.UserViewModelFactory

class FragmentAddNotification : Fragment() {

    private var _binding: FragmentAddNotificationBinding? = null
    private val binding get() = _binding!!
    private lateinit var notificationViewModel: NotificationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // khởi tạo viewmodel
        val factory = NotificationViewModelFactory(
            MyRoomDatabase.getDatabase(requireContext()).notificationDao()
        )
        notificationViewModel = ViewModelProvider(this,factory).get(NotificationViewModel::class.java)

        binding.btnAddNotification.setOnClickListener {
            addNewItem()
        }
        binding.btnExitToNotification.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAddNotification_to_fragmentNotifications)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun addNewItem() {
        notificationViewModel.addNewItem(
            "${binding.datePicker3.year}-${binding.datePicker3.month+1}-${binding.datePicker3.dayOfMonth}",
            binding.edtSubject.text.toString(),
            binding.edtDetailed.text.toString()
        )
        binding.edtSubject.setText("")
        binding.edtDetailed.setText("")
        Toast.makeText(getActivity(), "追加しました。", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}