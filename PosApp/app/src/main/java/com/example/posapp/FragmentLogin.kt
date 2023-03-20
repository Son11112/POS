package com.example.posapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

//class FragmentLogin : Fragment() {
//
//    private var _binding: FragmentLoginBinding? = null
//    private val binding get() = _binding!!
//
//    private val userViewModel: UserViewModel by activityViewModels { UserViewModelFactory(
//        MyRoomDatabase.getDatabase(requireContext()).userDao()) }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = FragmentLoginBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.btnLogin.setOnClickListener {
//            val employeeCode = binding.edtLoginCode.text.toString()
//            if (employeeCode.isNotEmpty()) {
//                try {
//                    val isAdmin = runBlocking { getRoleByEmployeeCode(employeeCode) }
//                    if (isAdmin) {
//                        findNavController().navigate(R.id.action_fragmentLogin_to_fragmentAdminLogin)
//                    } else {
//                        findNavController().navigate(R.id.action_fragmentLogin_to_fragmentOrders)
//                    }
//                } catch (e: Exception) {
//                    Toast.makeText(requireContext(), "社員コードが無効です", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                Toast.makeText(requireContext(), "社員コードを入力してください", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private suspend fun getRoleByEmployeeCode(employeeCode: String): Boolean {
//        val userDao = MyRoomDatabase.getDatabase(requireContext()).userDao()
//        val user = userDao.getUserByEmployeeCode(employeeCode)
//        return user.role == "admin"
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}



class FragmentLogin : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val button = view.findViewById<Button>(R.id.btnAd)
        button.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentLogin_to_fragmentAdminLogin)
            }
        val buttonStaff = view.findViewById<Button>(R.id.btnStaff)
        buttonStaff.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentLogin_to_fragmentOrders)
        }
        return view
    }

}