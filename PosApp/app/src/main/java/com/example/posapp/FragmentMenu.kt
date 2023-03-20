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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentMenu : Fragment() {
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var menuViewModel: MenuViewModel
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Khởi tạo ViewModel
        val factory = MenuViewModelFactory(
            MyRoomDatabase.getDatabase(requireContext()).menuDao()
        )
        menuViewModel = ViewModelProvider(this, factory).get(MenuViewModel::class.java)

        // Khởi tạo adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.MenuRecyclerview)

        // Lấy dữ liệu từ ViewModel và cập nhật lên RecyclerView
        menuViewModel.getAllMenu().observe(viewLifecycleOwner){ menu ->
            Log.d(TAG,"Menu $menu")
            var adapter = MenuAdapter(requireContext(),menu)
            recyclerView.adapter = adapter
        }
        binding.btnExt.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMenu_to_fragmentHome)
        }
        binding.btnAddMenu.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMenu_to_fragmentAddMenu)
        }
    }
}
