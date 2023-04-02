package com.example.posapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.posapp.adapter.DetailAdapter
import com.example.posapp.adapter.StatusAdapter
import com.example.posapp.data.MenuData
import com.example.posapp.data.MyRoomDatabase
import com.example.posapp.databinding.FragmentStatusBinding
import com.example.posapp.viewModel.MenuViewModel
import com.example.posapp.viewModel.MenuViewModelFactory
import com.example.posapp.viewModel.OrderViewModel
import com.example.posapp.viewModel.OrderViewModelFactory

class FragmentStatus : Fragment(), StatusAdapter.OnDetailButtonClickListener {

    private lateinit var statusAdapter: StatusAdapter
    private lateinit var detailAdapter: DetailAdapter
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var menuViewModel: MenuViewModel
    private var _binding: FragmentStatusBinding? = null
    private val binding get() = _binding!!
    private val menuDataList = mutableListOf<MenuData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Khởi tạo ViewModel
        val factory = MenuViewModelFactory(MyRoomDatabase.getDatabase(requireContext()).menuDao())
        val orderFactory = OrderViewModelFactory(
            MyRoomDatabase.getDatabase(requireContext()).orderDao(),
            MyRoomDatabase.getDatabase(requireContext()).orderFoodItemDao(),
            MyRoomDatabase.getDatabase(requireContext()).menuDao()
        )
        orderViewModel = ViewModelProvider(this, orderFactory).get(OrderViewModel::class.java)
        menuViewModel = ViewModelProvider(this, factory).get(MenuViewModel::class.java)

        // Lấy dữ liệu từ ViewModel và cập nhật menuDataList
        menuViewModel.getAllMenu().observe(viewLifecycleOwner) { menu ->
            menuDataList.clear()
            menuDataList.addAll(menu)
        }

        // Khởi tạo StatusAdapter
        statusAdapter = StatusAdapter(mutableListOf(), orderViewModel,this)

        // Khởi tạo DetailAdapter
        detailAdapter = DetailAdapter(menuDataList)

        // Thiết lập adapter cho RecyclerView status
        binding.recyclerviewStatus.adapter = statusAdapter

        // Thiết lập adapter cho RecyclerView detail
        binding.recyclerviewOrderDetail.adapter = detailAdapter

        // Lấy dữ liệu từ ViewModel và cập nhật lên RecyclerView status
        orderViewModel.getAllOrders().observe(viewLifecycleOwner) { orders ->
            statusAdapter.setData(orders)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentStatus_to_fragmentOrders)
        }
    }

    override fun onDetailButtonClick(orderId: String) {
        orderViewModel.orderFoodItemsByOrderId(orderId).observe(viewLifecycleOwner) { orderFoodItems ->
            detailAdapter.submitList(orderFoodItems)
        }
    }

    private fun navigateToFragmentOrder(orderId: Int) {
        val action = R.id.action_fragmentStatus_to_fragmentOrders
        val bundle = bundleOf("orderId" to orderId)
        findNavController().navigate(action, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}