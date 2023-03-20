package com.example.posapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.posapp.data.MenuData
import com.example.posapp.data.MyRoomDatabase
import com.example.posapp.data.OrderFoodItem
import com.example.posapp.data.OrdersData
import com.example.posapp.databinding.FragmentPayBinding
import com.example.posapp.viewModel.MenuViewModel
import com.example.posapp.viewModel.MenuViewModelFactory
import com.example.posapp.viewModel.OrderViewModel
import com.example.posapp.viewModel.OrderViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class FragmentPay : Fragment() {

    private lateinit var orderViewModel: OrderViewModel
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var _binding: FragmentPayBinding
    private val binding get() = _binding!!
    val menuData = mutableListOf<MenuData>()
    // Lấy thời gian hiện tại
    private val calendar = Calendar.getInstance()
    private val orderDates = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(calendar.time)
    private val orderTimes = SimpleDateFormat("HHmmss", Locale.getDefault()).format(calendar.time)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPayBinding.inflate(inflater, container, false)
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
            MyRoomDatabase.getDatabase(requireContext()).menuDao(),
            MyRoomDatabase.getDatabase(requireContext()).orderFoodItemDao(),
        )
        menuViewModel = ViewModelProvider(this, factory).get(MenuViewModel::class.java)
        orderViewModel = ViewModelProvider(this, orderFactory).get(OrderViewModel::class.java)

        val totalPrice = arguments?.getInt("totalPrice")
        binding.paymentAmountEditText.text = totalPrice.toString()+"円"

        binding.btnCash.isChecked = true
        binding.btnStatus.visibility = View.GONE

        binding.edtDepositAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateChange()
            }

            private fun calculateChange() {
                val depositAmount = binding.edtDepositAmount.text.toString()
                if (depositAmount.isNotEmpty()) {
                    val changeNumber = depositAmount.toInt() - totalPrice.toString().toInt()
                    if (changeNumber > 0) {
                        binding.edtChange.text = changeNumber.toString()+"円"
                    } else {
                        binding.edtChange.text = "0円"
                    }
                } else {
                    binding.edtChange.text = "0円"
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnCash.setOnCheckedChangeListener { buttonView, isChecked ->
            if (binding.btnCash.isChecked) {
                binding.edtDepositAmount.visibility = View.VISIBLE
                binding.tvDepositAmount.visibility = View.VISIBLE
                binding.edtChange.visibility = View.VISIBLE
                binding.tvChange.visibility = View.VISIBLE
                binding.imvPayPay.visibility = View.GONE
            }else{
                binding.imvPayPay.visibility = View.VISIBLE // Hiển thị imageView
                binding.edtDepositAmount.visibility = View.GONE // Ẩn editTextNumber
                binding.tvDepositAmount.visibility = View.GONE // Ẩn editTextNumber
                binding.edtChange.visibility = View.GONE
                binding.tvChange.visibility = View.GONE
            }
        }

        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentPay_to_fragmentCart)
        }

        binding.btnPay.setOnClickListener {
            if (binding.btnCash.isChecked){
                val depositAmount = binding.edtDepositAmount.text.toString()
                if (depositAmount.isNotEmpty()) {
                    val changeNumber = depositAmount.toInt() - totalPrice.toString().toInt()
                    if (changeNumber >= 0) {
                        binding.btnStatus.visibility = View.VISIBLE
                        binding.btnPay.visibility = View.GONE
                        Toast.makeText(context, "注文を確定しました", Toast.LENGTH_SHORT).show()
//                      cập nhật database
                        addNewItem()
                    }else{
                        Toast.makeText(context, "お金が足りませんでした！", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(context, "カートが空です", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnStatus.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentPay_to_fragmentStatus)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        // Nếu nút thanh toán bằng tiền mặt được chọn, ẩn imageView và hiển thị editTextNumber
        if (binding.btnCash.isChecked) {
            binding.edtDepositAmount.visibility = View.VISIBLE
            binding.tvDepositAmount.visibility = View.VISIBLE
            binding.edtChange.visibility = View.VISIBLE
            binding.tvChange.visibility = View.VISIBLE
            binding.imvPayPay.visibility = View.GONE
        } else {
            binding.imvPayPay.visibility = View.VISIBLE // Hiển thị imageView
            binding.edtDepositAmount.visibility = View.GONE // Ẩn editTextNumber
            binding.tvDepositAmount.visibility = View.GONE // Ẩn editTextNumber
            binding.edtChange.visibility = View.GONE
            binding.tvChange.visibility = View.GONE
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun addNewItem() {

        val filteredMenuData = menuData.filter { it.tempQuantityInCart > 0 }
        if (filteredMenuData.isNotEmpty()) {
            // Lấy thời gian hiện tại
            val calendar = Calendar.getInstance()
            val orderDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            val orderTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(calendar.time)
            val totalPrice = arguments?.getInt("totalPrice")
            val ordersData = OrdersData(
                orderId= orderDates+orderTimes,
                orderStatus = "on_order",
                totalPrice = totalPrice!!,
                orderDate = orderDate,
                orderTime = orderTime)

            orderViewModel.insertOrder(ordersData)
            filteredMenuData.map { menu ->
                val totalPrice = arguments?.getInt("totalPrice")
                val ordersData = OrdersData(
                    orderId= orderDates+orderTimes,
                    orderStatus = "on_order",
                    totalPrice = totalPrice!!,
                    orderDate = orderDate,
                    orderTime = orderTime)

                val orderFoodItems = OrderFoodItem(
                    orderId = ordersData.orderId,
                    foodItemId = menu.id,
                    quantityInCart = menu.tempQuantityInCart,
                    productQuantityInStock = menu.productQuantity,
                    productOrderImage = menu.productImage,
                    productName = menu.productName,
                    orderProductPrice = menu.productPrice,
                )
                orderViewModel.insertOrderItem(orderFoodItems)
            }

            filteredMenuData.forEach { menu ->
                CoroutineScope(Dispatchers.Main).launch {
                    menuViewModel.updateTempQuantityInCart(menu.id, 0)
                }
            }
        }else {
            Toast.makeText(context, "カートは空です", Toast.LENGTH_SHORT).show()
        }
    }
}