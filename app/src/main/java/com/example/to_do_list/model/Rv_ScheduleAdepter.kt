package com.example.to_do_list.model

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do_list.databinding.CustomItemscheduleBinding

class Rv_ScheduleAdepter(
    val activity: Activity,
    val list: List<ListSchedule>,
    val onItem: Item_Click
) :
    RecyclerView.Adapter<Rv_ScheduleAdepter.viewHolder>() {
    private lateinit var binding: CustomItemscheduleBinding

    inner class viewHolder(binding: CustomItemscheduleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        binding =
            CustomItemscheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.itemView.apply {
            binding.txtDay.text = list[position].day
            binding.txtTitleSchedule.text = list[position].title
            val start = list[position].timeStart.split(" ")
            val end = list[position].timeEnd.split(" ")
            binding.txtTime.text = "${start[1]} - ${end[1]}"
            binding.txtPlace.text = list[position].place
            binding.txtNote.text = list[position].notes

            holder.itemView.setOnLongClickListener {
                onItem.onLongClickNote(position)
                true
            }

            holder.itemView.setOnClickListener {
                onItem.onClickNote(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}