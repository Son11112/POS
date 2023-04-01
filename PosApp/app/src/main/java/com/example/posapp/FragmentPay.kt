package com.example.posapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.posapp.adapter.PayAdapter
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

    private lateinit var payAdapter: PayAdapter
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var _binding: FragmentPayBinding
    private val binding get() = _binding
    val menuData = mutableListOf<MenuData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        val orderFactory = OrderViewModelFactory(
            MyRoomDatabase.getDatabase(requireContext()).orderDao(),
            MyRoomDatabase.getDatabase(requireContext()).orderFoodItemDao(),
            MyRoomDatabase.getDatabase(requireContext()).menuDao()
        )
        orderViewModel = ViewModelProvider(this, orderFactory).get(OrderViewModel::class.java)

        payAdapter = PayAdapter(mutableListOf())
        binding.recyclerviewPay.adapter = payAdapter

      var totalPrice = 0

        binding.btnCash.isChecked = true
        binding.btnStatus.isEnabled = false
        if (binding.paymentAmountEditText.text.isEmpty()){
            binding.edtDepositAmount.visibility = View.GONE
        }else{
            binding.edtDepositAmount.visibility = View.VISIBLE
        }

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
                        binding.edtChange.text = changeNumber.toString() + "円"
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
            } else {
                binding.imvPayPay.visibility = View.VISIBLE // Hiển thị imageView
                binding.edtDepositAmount.visibility = View.GONE // Ẩn editTextNumber
                binding.tvDepositAmount.visibility = View.GONE // Ẩn editTextNumber
                binding.edtChange.visibility = View.GONE
                binding.tvChange.visibility = View.GONE
            }
        }

        binding.btnSearch.setOnClickListener {
            binding.btnPay.isEnabled = true
            if (binding.edtTableNumBerPay.text.isNotEmpty()) {
                val tableNumber = binding.edtTableNumBerPay.text.toString().toInt()

                orderViewModel.getUnpaidOrderByTableNumber(tableNumber).observe(viewLifecycleOwner) { order ->
                    if (order != null) {
                        binding.edtDepositAmount.visibility = View.VISIBLE

                        val orderId = order.orderId
                        orderViewModel.getOrderFoodItemDetailsByOrderId(orderId).observe(viewLifecycleOwner) { menuDataList ->
                            payAdapter.submitList(menuDataList)
                        }
                    } else {
                        Toast.makeText(context, "Không tìm thấy đơn hàng chưa thanh toán cho bàn này", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentPay_to_fragmentOrders)
        }

        binding.btnPay.setOnClickListener {
            if (binding.btnCash.isChecked) {
                if (binding.edtDepositAmount.text.toString().isNotEmpty()) {
                    val changeNumber =
                        binding.edtDepositAmount.text.toString().toInt() - totalPrice.toString()
                            .toInt()
                    if (changeNumber >= 0) {
                        binding.btnStatus.visibility = View.VISIBLE
                        Toast.makeText(context, "注文を確定しました", Toast.LENGTH_SHORT).show()
                        binding.btnStatus.isEnabled = true
                        binding.btnPayPay.isEnabled = false
                        binding.btnCash.isEnabled = false
                        binding.btnCancel.isEnabled = false
                        binding.btnPay.isEnabled = false
                    } else {
                        Toast.makeText(context, "お金が足りませんでした！", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "お金が足りませんでした", Toast.LENGTH_SHORT).show()
                }
            } else {
                binding.btnPayPay.isEnabled = false
                binding.btnCash.isEnabled = false
                binding.btnPay.isEnabled = false
                Toast.makeText(context, "注文を確定しました", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnStatus.setOnClickListener {
            binding.edtDepositAmount.setText(null)
            binding.edtChange.setText(null)
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
}