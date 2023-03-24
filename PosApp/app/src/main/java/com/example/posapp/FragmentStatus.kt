package com.example.posapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.posapp.adapter.StatusAdapter
import com.example.posapp.data.MyRoomDatabase
import com.example.posapp.data.OrdersData
import com.example.posapp.databinding.FragmentMenuBinding
import com.example.posapp.databinding.FragmentStatusBinding
import com.example.posapp.viewModel.OrderViewModel
import com.example.posapp.viewModel.OrderViewModelFactory

class FragmentStatus : Fragment() {

    private lateinit var statusAdapter: StatusAdapter
    private lateinit var orderViewModel: OrderViewModel
    private var _binding: FragmentStatusBinding? = null
    private val binding get() = _binding!!
    private val orderData = mutableListOf<OrdersData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = MyRoomDatabase.getDatabase(requireContext())
        val orderDao = db.orderDao()
        orderDao.getItems().observe(viewLifecycleOwner) { menu->
            orderData.addAll(menu)
        }

        // Khởi tạo ViewModel
        val orderFactory = OrderViewModelFactory(
            MyRoomDatabase.getDatabase(requireContext()).orderDao(),
            MyRoomDatabase.getDatabase(requireContext()).orderFoodItemDao(),
        )
        orderViewModel = ViewModelProvider(this, orderFactory).get(OrderViewModel::class.java)

        // Khởi tạo adapter
        statusAdapter = StatusAdapter(requireContext(), mutableListOf(),orderViewModel)
        val recyclerView = binding.recyclerviewStatus
        recyclerView.adapter = statusAdapter

        // Lấy dữ liệu từ ViewModel và cập nhật lên RecyclerView
        orderViewModel.getAllOrders().observe(viewLifecycleOwner){menu->
            statusAdapter.setData(menu)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentStatus_to_fragmentOrders)
        }
    }
}