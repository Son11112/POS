package com.example.posapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.data.ShiftsAdapter
import com.example.posapp.viewModel.ShiftsViewModel
import com.example.posapp.viewModel.ShiftsViewModelFactory
import com.example.posapp.data.MyRoomDatabase

class FragmentShifts : Fragment() {

    private lateinit var shiftsAdapter: ShiftsAdapter
    private lateinit var shiftsViewModel: ShiftsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shifts, container, false)

        val buttonAddShift = view.findViewById<Button>(R.id.btnToAddShift)
        buttonAddShift.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentShifts_to_fragmentAddShift)
    }
        val buttonExitToHome = view.findViewById<Button>(R.id.btnExitToHome)
        buttonExitToHome.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentShifts_to_fragmentHome)
        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Khởi tạo ViewModel

        val factory = ShiftsViewModelFactory(
            MyRoomDatabase.getDatabase(requireContext()).shiftsDao()
        )
        shiftsViewModel = ViewModelProvider(this, factory).get(ShiftsViewModel::class.java)

        // Khởi tạo adapter

        val recyclerView = view.findViewById<RecyclerView>(R.id.Shiftsrecycleview)

        // Lấy dữ liệu từ ViewModel và cập nhật lên RecyclerView
        shiftsViewModel.getAllShifts().observe(viewLifecycleOwner) { shifts ->
            Log.d(TAG,"Shifts $shifts")
            var adapter = ShiftsAdapter(requireContext(), shifts)
            recyclerView.adapter = adapter
        }
    }
}

