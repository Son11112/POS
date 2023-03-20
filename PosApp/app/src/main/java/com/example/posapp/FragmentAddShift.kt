package com.example.posapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.posapp.viewModel.ShiftsViewModel
import com.example.posapp.viewModel.ShiftsViewModelFactory
import com.example.posapp.data.ShiftsData
import com.example.posapp.data.MyRoomDatabase
import com.example.posapp.databinding.FragmentAddShiftBinding


class FragmentAddShift : Fragment() {

    private var _binding: FragmentAddShiftBinding? = null
    private val binding get() = _binding!!
    private var timeShifts: String = ""
    lateinit var item: ShiftsData
    private var selectedCheckbox: CheckBox? = null
    private val viewModel: ShiftsViewModel by activityViewModels {
        ShiftsViewModelFactory(
            MyRoomDatabase.getDatabase(requireContext()).shiftsDao()
        )
    }

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

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.edtName.text.toString(),
            "${binding.datePicker.year}-${binding.datePicker.month}-${binding.datePicker.dayOfMonth}",
            timeShifts,
        )
    }

    /**
     * Inserts the new Item into database and navigates up to list fragment.
     */
    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(
                binding.edtName.text.toString(),
                "${binding.datePicker.year}-${binding.datePicker.month}-${binding.datePicker.dayOfMonth}",
                timeShifts,
            )
            // Clear input fields
            binding.edtName.setText("")
            binding.cbxMorning.isChecked = false
            binding.cbxNoon.isChecked = false
            binding.cbxNight.isChecked = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddShift.setOnClickListener {
            Toast.makeText(getActivity(), "追加しました！", Toast.LENGTH_SHORT).show()
            addNewItem()
        }
        binding.btnExitToShifts.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentAddShift_to_fragmentShifts)
        }
    }

    /**
     * Called before fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}
