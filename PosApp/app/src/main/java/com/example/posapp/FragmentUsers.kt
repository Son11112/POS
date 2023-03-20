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
import com.example.posapp.adapter.UsersAdapter
import com.example.posapp.viewModel.UserViewModel
import com.example.posapp.viewModel.UserViewModelFactory
import com.example.posapp.data.MyRoomDatabase

class FragmentUsers : Fragment() {
    private lateinit var usersAdapter: UsersAdapter
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_users, container, false)
        val buttonExitToHome = view.findViewById<Button>(R.id.btnExtUserToHome)
        buttonExitToHome.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentUsers_to_fragmentHome)
        }
        val buttonToAddUsers = view.findViewById<Button>(R.id.btnToAddUsers)
        buttonToAddUsers.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentUsers_to_fragmentAddUsers)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // khởi tạo viewmodel
        val factory = UserViewModelFactory(
            MyRoomDatabase.getDatabase(requireContext()).userDao()
        )
        userViewModel = ViewModelProvider(this,factory).get(UserViewModel::class.java)

        // Khởi tạo adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.UsersRecycleview)
        userViewModel.getAllUser().observe(viewLifecycleOwner) { users ->
            Log.d(TAG,"Users $users")
            var adapter = UsersAdapter(requireContext(),users)
            recyclerView.adapter = adapter
        }
    }
}