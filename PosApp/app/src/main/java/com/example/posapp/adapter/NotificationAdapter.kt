package com.example.posapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.posapp.R
import com.example.posapp.data.NotificationData

class NotificationAdapter (private val context: Context, val dataset: List<NotificationData>
) :RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>(){

    class NotificationViewHolder(private val view:View) :RecyclerView.ViewHolder(view){
        val dateTextView :TextView = view.findViewById(R.id.tvDate)
        val subjectTextView :TextView = view.findViewById(R.id.tvSubject)
        val detailedTextView :TextView = view.findViewById(R.id.tvDetailed)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_notifications,parent,false)
        return NotificationViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = dataset[position]
        holder.dateTextView.text = item.date
        holder.subjectTextView.text = item.subject
        holder.detailedTextView.text = item.detailed
    }

    override fun getItemCount() = dataset.size
}