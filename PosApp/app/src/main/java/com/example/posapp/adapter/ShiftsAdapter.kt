package com.example.myapp.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.posapp.R
import com.example.posapp.data.ShiftsData

class ShiftsAdapter(
    private val context: Context,
    var dataset: List<ShiftsData>
) : RecyclerView.Adapter<ShiftsAdapter.ShiftsDataViewHolder>() {

    class ShiftsDataViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val employeeNameTextView: TextView = view.findViewById(R.id.tvName)
        val dateShiftsTextView: TextView = view.findViewById(R.id.tvTime)
        val timeShiftsTextView: TextView = view.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShiftsDataViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_shifts, parent, false)
        return ShiftsDataViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ShiftsDataViewHolder, position: Int) {
        val item = dataset[position]
        holder.employeeNameTextView.text = item.employeeName
        holder.dateShiftsTextView.text = item.dateShifts
        holder.timeShiftsTextView.text = item.timeShifts
    }

    override fun getItemCount() = dataset.size
}
