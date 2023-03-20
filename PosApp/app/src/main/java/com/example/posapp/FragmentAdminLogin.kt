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

class FragmentAdminLogin : Fragment() {

    private val MIN_PASSWORD_LENGTH = 4

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin_login, container, false)
        val edtLastLoginCode = view.findViewById<EditText>(R.id.edtLastLoginCode)
        val buttonLastLogin = view.findViewById<Button>(R.id.btnLastLogin)
        buttonLastLogin.setOnClickListener {
            val password = edtLastLoginCode.text.toString()
            if (password.length < MIN_PASSWORD_LENGTH) {
                Toast.makeText(activity, "4桁以上のコードを入れてください", Toast.LENGTH_SHORT).show()
            } else {
                findNavController().navigate(R.id.action_fragmentAdminLogin_to_fragmentHome)
            }
        }
        val buttonExitLogin = view.findViewById<Button>(R.id.btnExitLogin)
        buttonExitLogin.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAdminLogin_to_fragmentLogin)
        }
        return view
    }

}