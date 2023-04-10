package com.example.posapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.posapp.R.id
import com.example.posapp.data.MyRoomDatabase
import com.example.posapp.databinding.FragmentLoginBinding
import com.example.posapp.viewModel.UserViewModel
import com.example.posapp.viewModel.UserViewModelFactory
import kotlinx.coroutines.runBlocking

class FragmentLogin : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val employeeCode = binding.edtLoginCode.text.toString()
            if (employeeCode.isNotEmpty()) {
                try {
                    val isAdmin = runBlocking { getRoleByEmployeeCode(employeeCode) }
                    if (isAdmin) {
                        val action = R.id.action_fragmentLogin_to_fragmentAdminLogin
                        val bundle = bundleOf("employeeCode" to employeeCode)
                        findNavController().navigate(action, bundle)
                    } else {
                        findNavController().navigate(R.id.action_fragmentLogin_to_fragmentOrders)
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "社員コードが無効です", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "社員コードを入力してください", Toast.LENGTH_SHORT).show()
            }
        }

//        binding.btnAd.setOnClickListener {
//            findNavController().navigate(R.id.action_fragmentLogin_to_fragmentAdminLogin)
//        }
//        binding.btnStaff.setOnClickListener {
//            findNavController().navigate(R.id.action_fragmentLogin_to_fragmentOrders)
//        }
    }

    private suspend fun getRoleByEmployeeCode(employeeCode: String): Boolean {
        val userDao = MyRoomDatabase.getDatabase(requireContext()).userDao()
        val user = userDao.getUserByEmployeeCode(employeeCode)
        return user.role == "admin"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}