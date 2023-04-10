package com.example.posapp

import androidx.navigation.fragment.findNavController
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.posapp.adapter.OrderAdapter
import com.example.posapp.data.MenuData
import com.example.posapp.viewModel.MenuViewModel
import com.example.posapp.viewModel.MenuViewModelFactory
import com.example.posapp.data.MyRoomDatabase
import com.example.posapp.data.OrderFoodItem
import com.example.posapp.data.OrdersData
import com.example.posapp.databinding.FragmentOrdersBinding
import com.example.posapp.viewModel.OrderViewModel
import com.example.posapp.viewModel.OrderViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FragmentOrders : Fragment() {

    private lateinit var orderAdapter: OrderAdapter
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var orderViewModel: OrderViewModel
    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    val menuData = mutableListOf<MenuData>()
    private val calendar = Calendar.getInstance()
    private val orderDates = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(calendar.time)
    private val orderTimes = SimpleDateFormat("HHmmss", Locale.getDefault()).format(calendar.time)
    private val orderDate =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    private val orderTime =
        SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(calendar.time)

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
            MyRoomDatabase.getDatabase(requireContext()).menuDao()
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

        binding.btnOrder.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentOrders_to_fragmentStatus)
        }

        binding.btnCart.setOnClickListener {
            if (binding.edtTableNumBer.text.toString().isNotEmpty()) {
                viewLifecycleOwner.lifecycleScope.launch {
                    val selectedItems = orderAdapter.getSelectedItems()
                    if (selectedItems.isNotEmpty()) {
                        val menuItems = selectedItems.map { it.first }
                        showDialogAdd(menuItems)
                    } else {
                        Toast.makeText(context, "カートが空です", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "テーブル番号を入れてください", Toast.LENGTH_SHORT).show()
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

        binding.btnPay.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentOrders_to_fragmentPay)
        }
    }

    private suspend fun insertOrder(selectedItems: List<MenuData>) {
        val tableNumber = binding.edtTableNumBer.text.toString().toInt()
        val selectedItemsWithQuantity = orderAdapter.getSelectedItems()
        var totalPrice = 0
        for (menuItem in selectedItems) {
            val quantity = orderAdapter.getQuantityAt(menuItem.id)
            totalPrice += quantity * menuItem.productPrice
        }
        val orderData =OrdersData(
            orderId = orderDates + orderTimes,
            orderStatus = "on_order",
            totalPrice = totalPrice,
            orderDate = orderDate,
            orderTime = orderTime,
            tableNumber = tableNumber,
            payMethod = "unpaid"
        )
        orderViewModel.insertOrder(orderData)
        for ((menuItem, quantityInCart) in selectedItemsWithQuantity) {
            val orderFoodItem = OrderFoodItem(
                orderId = orderDates + orderTimes,
                foodItemId = menuItem.id,
                quantityInCart = quantityInCart
            )
            orderViewModel.insertOrderItem(orderFoodItem)
            menuViewModel.updateQuantityInStock(menuItem.id, menuItem.productQuantity-quantityInCart)
            binding.edtTableNumBer.setText("")
            orderAdapter.resetTempQuantityMap()
        }
    }

    private fun showDialogAdd(menuItems: List<MenuData>) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("新しい注文内容確認")
        builder.setMessage("注文内容はこれでよろしいですか?")

        builder.setPositiveButton("注文送信") { _, _ ->
            CoroutineScope(Dispatchers.Main).launch {
                insertOrder(menuItems)
                Toast.makeText(requireContext(), "新しい注文を追加しました", Toast.LENGTH_SHORT)
                    .show()
            }
//            findNavController().navigate(R.id.action_fragmentOrders_to_fragmentStatus)
        }
        builder.setNegativeButton("キャンセル") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
