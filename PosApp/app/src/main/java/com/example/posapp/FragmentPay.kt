package com.example.posapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.posapp.adapter.PayAdapter
import com.example.posapp.data.MenuData
import com.example.posapp.data.MyRoomDatabase
import com.example.posapp.data.OrdersData
import com.example.posapp.databinding.FragmentPayBinding
import com.example.posapp.viewModel.OrderViewModel
import com.example.posapp.viewModel.OrderViewModelFactory


class FragmentPay : Fragment(), PayAdapter.OnCheckedChangeListener{
    private lateinit var payAdapter: PayAdapter
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var _binding: FragmentPayBinding
    private val binding get() = _binding
    private val selectedItems = mutableSetOf<OrdersData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPayBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun updatePaymentMethodVisibility() {
        if (binding.btnCash.isChecked) {
            binding.edtDepositAmount.visibility = View.VISIBLE
            binding.tvDepositAmount.visibility = View.VISIBLE
            binding.edtChange.visibility = View.VISIBLE
            binding.tvChange.visibility = View.VISIBLE
            binding.imvPayPay.visibility = View.GONE
        } else {
            binding.imvPayPay.visibility = View.VISIBLE
            binding.edtDepositAmount.visibility = View.GONE
            binding.tvDepositAmount.visibility = View.GONE
            binding.edtChange.visibility = View.GONE
            binding.tvChange.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.paymentRadioGroup.setOnCheckedChangeListener { _, _ ->
            updatePaymentMethodVisibility()
        }

        val onCheckedChangeListener = object : PayAdapter.OnCheckedChangeListener {
            override fun onItemChecked(item: OrdersData, isChecked: Boolean) {
                if (isChecked) {
                    selectedItems.add(item)
                } else {
                    selectedItems.remove(item)
                }
                val totalPrice = selectedItems.sumBy { it.totalPrice }
                binding.paymentAmountEditText.setText(totalPrice.toString())
            }
        }

        val db = MyRoomDatabase.getDatabase(requireContext())
        val orderDao = db.orderDao()
        orderDao.getItems().observe(viewLifecycleOwner) { ordersData ->
            payAdapter.updateDataset(ordersData)
        }

        val orderFactory = OrderViewModelFactory(
            MyRoomDatabase.getDatabase(requireContext()).orderDao(),
            MyRoomDatabase.getDatabase(requireContext()).orderFoodItemDao(),
            MyRoomDatabase.getDatabase(requireContext()).menuDao()
        )
        orderViewModel = ViewModelProvider(this, orderFactory).get(OrderViewModel::class.java)

        payAdapter = PayAdapter(mutableListOf(), onCheckedChangeListener)
        binding.recyclerviewPay.adapter = payAdapter

        binding.btnCash.isChecked = true

        if (binding.paymentAmountEditText.text.toString().toInt() != 0){
            binding.edtDepositAmount.visibility = View.VISIBLE
        }else{
            binding.edtDepositAmount.visibility = View.GONE
        }

         fun calculateChange() {
            val paymentAmount = binding.paymentAmountEditText.text.toString().toIntOrNull() ?: 0
            val depositAmount = binding.edtDepositAmount.text.toString().toIntOrNull() ?: 0

            val changeNumber = depositAmount - paymentAmount
            if (changeNumber > 0) {
                binding.edtChange.text = changeNumber.toString() + "円"
            } else {
                binding.edtChange.text = "0円"
            }
        }


        binding.paymentAmountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val paymentAmount = s.toString().toIntOrNull() ?: 0
                if (paymentAmount != 0) {
                    binding.edtDepositAmount.visibility = View.VISIBLE
                } else {
                    binding.edtDepositAmount.visibility = View.GONE
                }
                calculateChange()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edtDepositAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateChange()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentPay_to_fragmentOrders)
        }

        binding.btnPay.setOnClickListener {
            if (binding.btnCash.isChecked) {
                if (binding.edtDepositAmount.text.toString().isNotEmpty()) {
                    val changeNumber =
                        binding.edtDepositAmount.text.toString().toInt() -  binding.paymentAmountEditText.text.toString().toInt()
                    if (changeNumber >= 0) {
                        binding.btnStatus.visibility = View.VISIBLE
                        Toast.makeText(context, "注文を確定しました", Toast.LENGTH_SHORT).show()
                        binding.btnPayPay.isEnabled = false
                        binding.btnCash.isEnabled = false
                        binding.btnPay.isEnabled = false

                        for (selectedOrder in selectedItems) {
                                selectedOrder.payMethod = "現金払い"
                            orderViewModel.updateOrder(selectedOrder)
                        }
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
                for (selectedOrder in selectedItems) {
                    selectedOrder.payMethod = "paypay払い"
                    orderViewModel.updateOrder(selectedOrder)
                }
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

    override fun onItemChecked(item: OrdersData, isChecked: Boolean) {
        if (isChecked) {
            selectedItems.add(item)
        } else {
            selectedItems.remove(item)
        }
        val totalPrice = selectedItems.sumBy { it.totalPrice }
        binding.paymentAmountEditText.setText(totalPrice.toString())
    }
}