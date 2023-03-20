package com.example.posapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

class FragmentSalesAnalysis : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sales_analysis, container, false)
        val buttonExitToHome = view.findViewById<Button>(R.id.btnExitToHome)
        buttonExitToHome.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentSalesAnalysis_to_fragmentHome)
        }
        return view
    }
}