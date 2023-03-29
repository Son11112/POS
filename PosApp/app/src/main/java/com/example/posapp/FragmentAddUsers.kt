package com.example.posapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.posapp.PasswordUtils.PasswordUtils
import com.example.posapp.viewModel.*
import com.example.posapp.data.MyRoomDatabase
import com.example.posapp.databinding.FragmentAddUsersBinding


class FragmentAddUsers : Fragment() {

    // Add a variable to store the role
    private var role: String = ""
    private var pass: String = ""
    private var selectedCheckbox: CheckBox? = null
    private var _binding: FragmentAddUsersBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddUsersBinding.inflate(inflater, container, false)
        val view = binding.root
        val checkboxAdmin = binding.cbxAdmin
        val checkboxStaff = binding.cbxStaff
        val editTextPass: EditText = view.findViewById(R.id.edtPass)

        checkboxAdmin.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedCheckbox = checkboxAdmin
                checkboxStaff.isChecked = false
                editTextPass.text.clear()
                editTextPass.visibility = View.VISIBLE
                // Set the role to "admin" if checkboxAdmin is checked
                role = "admin"
            }
        }

        checkboxStaff.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedCheckbox = checkboxStaff
                checkboxAdmin.isChecked = false
//                editTextEmployerCode.text.clear()
                editTextPass.visibility = View.GONE
                // Set the role to "staff" if checkboxStaff is checked
                role = "staff"
                pass = "1"
            }
        }
        return view
    }
    private fun addStaff() {
        userViewModel.addNewItem(
            role,
            binding.edtName.text.toString(),
            binding.edtEmployerCode.text.toString(),
            pass,
            "${binding.datePicker.year}-${binding.datePicker.month}-${binding.datePicker.dayOfMonth}",
            binding.edtNumberPhone.text.toString()
        )
        // Clear input fields
        binding.edtEmployerCode.setText("")
        binding.edtName.setText("")
        binding.edtPass.setText("")
        binding.edtNumberPhone.setText("")
    }

    private fun addAd() {
        val hashedPassword = PasswordUtils.hashPassword(binding.edtPass.text.toString())
        userViewModel.addNewItem(
                role,
                binding.edtName.text.toString(),
                binding.edtEmployerCode.text.toString(),
                hashedPassword,
            "${binding.datePicker.year}-${binding.datePicker.month+1}-${binding.datePicker.dayOfMonth}",
            binding.edtNumberPhone.text.toString()
            )
            // Clear input fields
            binding.edtEmployerCode.setText("")
            binding.edtName.setText("")
            binding.edtPass.setText("")
            binding.edtNumberPhone.setText("")
        }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = UserViewModelFactory(MyRoomDatabase.getDatabase(requireContext()).userDao())
        userViewModel = ViewModelProvider(this,factory).get(UserViewModel::class.java)

        binding.btnExitToUsers.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAddUsers_to_fragmentUsers)
        }
        binding.btnAddUser.setOnClickListener {
            val checkboxAdmin = binding.cbxAdmin
            val checkboxStaff = binding.cbxStaff
            val editEmployerName: EditText = view.findViewById(R.id.edtName)
            val editTextEmployerCode: EditText = view.findViewById(R.id.edtEmployerCode)
            val editTextPass: EditText = view.findViewById(R.id.edtPass)

            if (checkboxAdmin.isChecked &&
                editTextEmployerCode.text.isNotBlank() &&
                editTextPass.text.isNotBlank() &&
                editEmployerName.text.isNotBlank()
            ) {
                // Kiểm tra employeeCode đã tồn tại hay chưa
                val employeeCode = binding.edtEmployerCode.text.toString()
                userViewModel.checkEmployeeCodeExist(employeeCode)
                    .observe(viewLifecycleOwner) { isExist ->
                        if (isExist) {
                            Toast.makeText(context, "社員コードが重複しています", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(getActivity(), "追加しました。", Toast.LENGTH_LONG).show()
                            addAd()
                        }
                    }

            } else if (checkboxStaff.isChecked &&
                editTextEmployerCode.text.isNotBlank() &&
                editEmployerName.text.isNotBlank()
            ) {
                val employeeCode = binding.edtEmployerCode.text.toString()
                userViewModel.checkEmployeeCodeExist(employeeCode)
                    .observe(viewLifecycleOwner) { isExist ->
                        if (isExist) {
                            Toast.makeText(context, "社員コードが重複しています", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(getActivity(), "追加しました。", Toast.LENGTH_LONG).show()
                            addStaff()
                        }
                    }
            } else {
                Toast.makeText(getActivity(), "未記入の項目があります！", Toast.LENGTH_LONG).show()
            }
        }
    }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }


