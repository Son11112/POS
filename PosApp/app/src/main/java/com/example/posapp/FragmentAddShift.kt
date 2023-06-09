package com.example.posapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.posapp.viewModel.ShiftsViewModel
import com.example.posapp.viewModel.ShiftsViewModelFactory
import com.example.posapp.data.ShiftsData
import com.example.posapp.data.MyRoomDatabase
import com.example.posapp.databinding.FragmentAddShiftBinding
import com.example.posapp.viewModel.UserViewModel
import com.example.posapp.viewModel.UserViewModelFactory


class FragmentAddShift : Fragment() {

    private var _binding: FragmentAddShiftBinding? = null
    private val binding get() = _binding!!
    private var timeShifts: String = ""
    lateinit var item: ShiftsData
    private var selectedCheckbox: CheckBox? = null
    private lateinit var shiftsViewModel: ShiftsViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddShiftBinding.inflate(inflater, container, false)
        val view = binding.root
        val checkboxMorning = binding.cbxMorning
        val checkboxNoon = binding.cbxNoon
        val checkboxNight = binding.cbxNight

        checkboxMorning.setOnClickListener {
            selectedCheckbox = checkboxMorning
            checkboxNoon.isChecked = false
            checkboxNight.isChecked = false
            timeShifts = "早朝 "
        }

        checkboxNoon.setOnClickListener {
            selectedCheckbox = checkboxNoon
            checkboxMorning.isChecked = false
            checkboxNight.isChecked = false
            timeShifts = "昼勤 "
        }

        checkboxNight.setOnClickListener {
            selectedCheckbox = checkboxNoon
            checkboxMorning.isChecked = false
            checkboxNoon.isChecked = false
            timeShifts = "夕方 "
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
        binding.spnName.adapter = adapter

        val shiftsFactory = ShiftsViewModelFactory(MyRoomDatabase.getDatabase(requireContext()).shiftsDao())
        shiftsViewModel = ViewModelProvider(this, shiftsFactory).get(ShiftsViewModel::class.java)

        val userFactory = UserViewModelFactory(MyRoomDatabase.getDatabase(requireContext()).userDao())
        userViewModel = ViewModelProvider(this, userFactory).get(UserViewModel::class.java)

        userViewModel.getStaffList().observe(viewLifecycleOwner) { staffList ->
            val staffNames = staffList.map { it.employeeName }
            adapter.clear()
            adapter.addAll(staffNames)
            adapter.notifyDataSetChanged()
        }

        binding.btnAddShift.setOnClickListener {
            Toast.makeText(getActivity(), "追加しました！", Toast.LENGTH_SHORT).show()
            addNewItem()
        }
        binding.btnExitToShifts.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAddShift_to_fragmentHome)
        }

        binding.btnShifts.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAddShift_to_fragmentShifts)
        }
    }

    private fun addNewItem() {
        val selectedName = binding.spnName.selectedItem.toString()
        shiftsViewModel.addNewItem(selectedName, "${binding.datePicker.year}-${binding.datePicker.month+1}-${binding.datePicker.dayOfMonth}", timeShifts,)
        binding.cbxMorning.isChecked = false
        binding.cbxNoon.isChecked = false
        binding.cbxNight.isChecked = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}
