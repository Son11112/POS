package com.example.posapp

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.posapp.adapter.CartAdapter
import com.example.posapp.adapter.CartAdapterListener
import com.example.posapp.data.MenuData
import com.example.posapp.data.MyRoomDatabase
import com.example.posapp.databinding.FragmentCartBinding
import com.example.posapp.viewModel.MenuViewModel
import com.example.posapp.viewModel.MenuViewModelFactory
import kotlinx.coroutines.launch

class FragmentCart : Fragment() {

    private lateinit var menuViewModel: MenuViewModel
    private lateinit var cartAdapter: CartAdapter
    private var _binding: FragmentCartBinding? = null
    private  val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Khởi tạo ViewModel
        val factory = MenuViewModelFactory(
            MyRoomDatabase.getDatabase(requireContext()).menuDao()
        )
        menuViewModel = ViewModelProvider(this, factory).get(MenuViewModel::class.java)

        // Khởi tạo adapter
        cartAdapter = CartAdapter(requireContext(), mutableListOf(), object : CartAdapterListener {
            override fun onTotalPriceChanged(totalPrice: Int) {
                binding.tvTotalPrice.text = totalPrice.toString()
            }
        })
        val recyclerView = view.findViewById<RecyclerView>(R.id.productListRecyclerview)

        // Lấy dữ liệu từ ViewModel và cập nhật lên RecyclerView
        menuViewModel.getTempMenu().observe(viewLifecycleOwner) { menu ->
            Log.d(TAG, "Menu $menu")
            cartAdapter = CartAdapter(requireContext(), menu, object : CartAdapterListener {
                override fun onTotalPriceChanged(totalPrice: Int) {
                    binding.tvTotalPrice.text = totalPrice.toString()
                }
            })
            recyclerView.adapter = cartAdapter
            binding.tvTotalPrice.text = cartAdapter.getTotalPrice().toString()
        }

        binding.btnCancel.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                updateCartItems()
                findNavController().navigate(R.id.action_fragmentCart_to_fragmentOrders)
            }
        }

        binding.btnDelete.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                menuViewModel.resetTempQuantity()
            }
            menuViewModel.getTempMenu().observe(viewLifecycleOwner) { menu ->
                Log.d(TAG, "Menu $menu")
                var adapter = CartAdapter(requireContext(), menu, object : CartAdapterListener {
                    override fun onTotalPriceChanged(totalPrice: Int) {
                        binding.tvTotalPrice.text = totalPrice.toString()
                    }
                })
                recyclerView.adapter = adapter
            }
            binding.tvTotalPrice.text = cartAdapter.getTotalPrice().toString()
        }

        binding.btnPay.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                updateCartItems()
            }
            if (binding.tvTotalPrice.text.toString().toInt()> 0) {
                val bundle = Bundle().apply {
                    putInt("totalPrice", binding.tvTotalPrice.text.toString().toInt())
                }
                    findNavController().navigate(R.id.action_fragmentCart_to_fragmentPay, bundle)
            }else{
                Toast.makeText(context, "カートが空です", Toast.LENGTH_SHORT).show()
        }
    }
    }
        @SuppressLint("SuspiciousIndentation")
        suspend fun updateCartItems() {

            val itemCount = cartAdapter.itemCount
            val items = mutableListOf<MenuData>()
            for (i in 0 until itemCount) {
                val item = cartAdapter.getItem(i)
                items.add(item)
            }
                for (menuData in items) {
                    // Cập nhật số lượng tempQuantityInCart vào cơ sở dữ liệu
                    menuViewModel.updateTempQuantityInCart(menuData.id, menuData.tempQuantityInCart)
                }
            }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
