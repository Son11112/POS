package com.example.posapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
    private val viewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(
            MyRoomDatabase.getDatabase(requireContext()).userDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddUsersBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize views here
        val checkboxAdmin = binding.cbxAdmin
        val checkboxStaff = binding.cbxStaff
        val editTextEmployerCode: EditText = view.findViewById(R.id.edtEmployerCode)
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
                pass = "123456"
            }
        }
        return view
    }
    fun addStaff() {
        viewModel.addNewItem(
            role,
            binding.edtName.text.toString(),
            binding.edtEmployerCode.text.toString(),
            pass
        )
        // Clear input fields
        binding.edtEmployerCode.setText("")
        binding.edtName.setText("")
        binding.edtPass.setText("")
    }

    fun addNewItem() {
            viewModel.addNewItem(
                role,
                binding.edtName.text.toString(),
                binding.edtEmployerCode.text.toString(),
                binding.edtPass.text.toString(),
            )
            // Clear input fields
            binding.edtEmployerCode.setText("")
            binding.edtName.setText("")
            binding.edtPass.setText("")
        }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                Toast.makeText(getActivity(), "追加しました。", Toast.LENGTH_LONG).show()
                addNewItem()

            } else if (checkboxStaff.isChecked &&
                editTextEmployerCode.text.isNotBlank() &&
                editEmployerName.text.isNotBlank()
            ) {
                Toast.makeText(getActivity(), "追加しました。", Toast.LENGTH_LONG).show()
                addStaff()
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


