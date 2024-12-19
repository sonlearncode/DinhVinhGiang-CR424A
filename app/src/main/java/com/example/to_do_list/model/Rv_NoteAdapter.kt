package com.example.to_do_list.model

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do_list.databinding.CustomItemnoteBinding

class Rv_NoteAdapter(
    val activity: Activity,
    val list: List<ListNote>,
    val onItem: Item_Click
) :
    RecyclerView.Adapter<Rv_NoteAdapter.viewHolder>() {
    private lateinit var binding: CustomItemnoteBinding

    inner class viewHolder(binding: CustomItemnoteBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        binding = CustomItemnoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.itemView.apply {
            binding.txtNote.text = list[position].txt_note
            binding.txtNoteDate.text = list[position].txt_noteDate

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