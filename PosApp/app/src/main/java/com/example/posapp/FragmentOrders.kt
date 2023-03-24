package com.example.posapp

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import androidx.navigation.fragment.findNavController
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.posapp.adapter.OrderAdapter
import com.example.posapp.data.MenuData
import com.example.posapp.viewModel.MenuViewModel
import com.example.posapp.viewModel.MenuViewModelFactory
import com.example.posapp.data.MyRoomDatabase
import com.example.posapp.databinding.FragmentOrdersBinding
import com.example.posapp.viewModel.OrderViewModel
import com.example.posapp.viewModel.OrderViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class FragmentOrders : Fragment() {

    private lateinit var orderAdapter: OrderAdapter
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var orderViewModel: OrderViewModel
    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    val menuData = mutableListOf<MenuData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Đọc dữ liệu từ database vào biến menuData
        val db = MyRoomDatabase.getDatabase(requireContext())
        val menuDao = db.menuDao()
        menuDao.getItems().observe(viewLifecycleOwner) { menu ->
            menuData.addAll(menu)
        }

        // Khởi tạo ViewModel
        val factory = MenuViewModelFactory(MyRoomDatabase.getDatabase(requireContext()).menuDao())
        val orderFactory = OrderViewModelFactory(
            MyRoomDatabase.getDatabase(requireContext()).orderDao(),
            MyRoomDatabase.getDatabase(requireContext()).orderFoodItemDao(),
        )
        menuViewModel = ViewModelProvider(this, factory).get(MenuViewModel::class.java)
        orderViewModel = ViewModelProvider(this, orderFactory).get(OrderViewModel::class.java)

        // Khởi tạo adapter
        orderAdapter = OrderAdapter(requireContext(), mutableListOf(), menuViewModel)
        val recyclerView = binding.OrderRecyclerview
        recyclerView.adapter = orderAdapter

        // Lấy dữ liệu từ ViewModel và cập nhật lên RecyclerView
        menuViewModel.getAllMenu().observe(viewLifecycleOwner) { menu ->
            orderAdapter.setData(menu)
        }

//        binding.btnAdd.setOnClickListener {
//            viewLifecycleOwner.lifecycleScope.launch {
//                updateCartItems()
//            }
//        }

        binding.btnOrder.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                updateCartItems()
                findNavController().navigate(R.id.action_fragmentOrders_to_fragmentStatus)
            }
        }

        binding.btnCart.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                updateCartItems()
                findNavController().navigate(R.id.action_fragmentOrders_to_fragmentCart)
            }
        }

        binding.btnMainFood.setOnClickListener {
                menuViewModel.getMainFoods().observe(viewLifecycleOwner) { menu ->
                    orderAdapter.setData(menu)
                }
            }
        binding.btnDessert.setOnClickListener {
                menuViewModel.getDesserts().observe(viewLifecycleOwner) { menu ->
                    orderAdapter.setData(menu)
                }
            }
        binding.btnDrink.setOnClickListener {
                menuViewModel.getDrinks().observe(viewLifecycleOwner) { menu ->
                    orderAdapter.setData(menu)
                }
            }
        binding.btnLogOut.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentOrders_to_fragmentLogin)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun updateCartItems() {

        val itemCount = orderAdapter.itemCount
        val menuData = mutableListOf<MenuData>()
        for (i in 0 until itemCount) {
            val item = orderAdapter.getItem(i)
            menuData.add(item)
        }
            for (item in menuData) {
                // Cập nhật số lượng tempQuantityInCart vào cơ sở dữ liệu
                menuViewModel.updateTempQuantityInCart(item.id, item.tempQuantityInCart)
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
