package com.example.posapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.posapp.adapter.MenuAdapter
import com.example.posapp.data.MenuData
import com.example.posapp.viewModel.MenuViewModel
import com.example.posapp.viewModel.MenuViewModelFactory
import com.example.posapp.data.MyRoomDatabase
import com.example.posapp.databinding.FragmentMenuBinding

class FragmentMenu : Fragment() {

    private lateinit var menuViewModel: MenuViewModel
    private lateinit var menuAdapter: MenuAdapter
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    val menuData = mutableListOf<MenuData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
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

        menuViewModel = ViewModelProvider(this, factory).get(MenuViewModel::class.java)

        // Khởi tạo adapter
        menuAdapter = MenuAdapter(requireContext(), mutableListOf(), menuViewModel)
        val recyclerView = view.findViewById<RecyclerView>(R.id.MenuRecyclerview)

        // Lấy dữ liệu từ ViewModel và cập nhật lên RecyclerView
        menuViewModel.getAllMenu().observe(viewLifecycleOwner) { menu ->
            menuAdapter = MenuAdapter(requireContext(), menu,menuViewModel)
            recyclerView.adapter = menuAdapter
        }
        binding.btnExt.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMenu_to_fragmentHome)
        }
        binding.btnAddMenu.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMenu_to_fragmentAddMenu)
        }
    }
}

