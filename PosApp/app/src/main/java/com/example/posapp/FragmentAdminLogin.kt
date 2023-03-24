package com.example.posapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.posapp.data.MyRoomDatabase
import com.example.posapp.databinding.FragmentAdminLoginBinding
import com.example.posapp.viewModel.UserViewModel
import kotlinx.coroutines.runBlocking

class FragmentAdminLogin : Fragment() {

    private var _binding: FragmentAdminLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLastLogin.setOnClickListener {
            val password = binding.edtLastLoginCode.text.toString()
            if (password.isNotEmpty()) {
                try {
                    val isAdmin = runBlocking { getRoleByEmployeeCode(password) }
                    if (isAdmin) {
                        findNavController().navigate(R.id.action_fragmentAdminLogin_to_fragmentHome)
                    } else {
                        Toast.makeText(context, "パスワード正しくありません！", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "社員コードが無効です", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "社員コードを入力してください", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnExitLogin.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAdminLogin_to_fragmentLogin)
        }
    }

    private suspend fun getRoleByEmployeeCode(password: String): Boolean {
        val userDao = MyRoomDatabase.getDatabase(requireContext()).userDao()
        val user = userDao.getUserByEmployeePass(password)
        return user.role == "admin"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




//class FragmentAdminLogin : Fragment() {
//
//    private val MIN_PASSWORD_LENGTH = 4
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_admin_login, container, false)
//        val edtLastLoginCode = view.findViewById<EditText>(R.id.edtLastLoginCode)
//        val buttonLastLogin = view.findViewById<Button>(R.id.btnLastLogin)
//        buttonLastLogin.setOnClickListener {
//            val password = edtLastLoginCode.text.toString()
//            if (password.length < MIN_PASSWORD_LENGTH) {
//                Toast.makeText(activity, "4桁以上のコードを入れてください", Toast.LENGTH_SHORT).show()
//            } else {
//                findNavController().navigate(R.id.action_fragmentAdminLogin_to_fragmentHome)
//            }
//        }
//        val buttonExitLogin = view.findViewById<Button>(R.id.btnExitLogin)
//        buttonExitLogin.setOnClickListener {
//            findNavController().navigate(R.id.action_fragmentAdminLogin_to_fragmentLogin)
//        }
//        return view
//    }
//}