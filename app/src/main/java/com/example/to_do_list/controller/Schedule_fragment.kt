package com.example.to_do_list.controller

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.to_do_list.R
import com.example.to_do_list.data.MyHelper
import com.example.to_do_list.databinding.CustomDialogConfirmBinding
import com.example.to_do_list.databinding.FragmentScheduleViewBinding
import com.example.to_do_list.model.Item_Click
import com.example.to_do_list.model.ListSchedule
import com.example.to_do_list.model.Rv_ScheduleAdepter

class Schedule_fragment : Fragment(R.layout.fragment_schedule_view) {
    private lateinit var binding: FragmentScheduleViewBinding
    private lateinit var adapter: Rv_ScheduleAdepter
    private lateinit var list: MutableList<ListSchedule>
    private lateinit var rs: Cursor
    private lateinit var db: SQLiteDatabase
    private lateinit var dialog: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentScheduleViewBinding.bind(view)

        val helper = MyHelper(requireContext())
        list = mutableListOf()
        db = helper.readableDatabase
        getValueDataBase()

        if (list.isEmpty()) {
            binding.txtNotification.visibility = View.VISIBLE
            binding.rvListSchedule.visibility = View.GONE
        } else {
            binding.txtNotification.visibility = View.VISIBLE
            binding.rvListSchedule.visibility = View.VISIBLE
        }
        setUpAdapter()
    }

    private fun getValueDataBase() {
        if (::rs.isInitialized && !rs.isClosed) {
            rs.close()
        }

        rs = db.rawQuery("SELECT * FROM SCHEDULE", null)
        list.clear()

        if (rs.moveToFirst()) {
            do {
                val scheduleID = rs.getString(rs.getColumnIndexOrThrow("_id"))
                val day = rs.getString(rs.getColumnIndexOrThrow("DAY") ?: -1) ?: "0"
                val title = rs.getString(rs.getColumnIndexOrThrow("TITLE"))
                val fullDay = rs.getInt(rs.getColumnIndexOrThrow("FULLDAY"))
                val timeStart = rs.getString(rs.getColumnIndexOrThrow("TIMESTART"))
                val timeEnd = rs.getString(rs.getColumnIndexOrThrow("TIMEEND"))
                val repeat = rs.getInt(rs.getColumnIndexOrThrow("REPEAT"))
                val remind = rs.getInt(rs.getColumnIndexOrThrow("REMINDER"))
                val place = rs.getString(rs.getColumnIndexOrThrow("PLACE"))
                val notes = rs.getString(rs.getColumnIndexOrThrow("NOTES"))
                list.add(
                    ListSchedule(
                        scheduleID,
                        day,
                        title,
                        fullDay,
                        timeStart,
                        timeEnd,
                        repeat,
                        remind,
                        place,
                        notes
                    )
                )
            } while (rs.moveToNext())
        }

        rs.close()
    }

    private fun setUpAdapter() {
        list = list.sortedBy { it.day.toInt() }.toMutableList()
        adapter = Rv_ScheduleAdepter(
            requireActivity(),
            list,
            object : Item_Click {
                override fun onLongClickNote(pos: Int) {
                    Toast.makeText(requireActivity(), "${pos} long", Toast.LENGTH_SHORT).show()
                    removeSchedule(pos)
                }

                override fun onClickNote(pos: Int) {
                    addEventClickItem(pos)
                }
            })

        binding.rvListSchedule.adapter = adapter
        binding.rvListSchedule.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun removeSchedule(pos: Int) {
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        val dialogBinding =
            CustomDialogConfirmBinding.inflate(LayoutInflater.from(requireActivity()))
        dialogBuilder.setView(dialogBinding.root)

        dialogBinding.imgIcon.setImageResource(R.drawable.img_warning)

        dialogBinding.txtContent.text = "Are you sure this is done?"
        dialogBinding.btnYes.setOnClickListener {
            val scheduleToRemove = list[pos]

            db.delete(
                "SCHEDULE", "_id = ?", arrayOf(scheduleToRemove._id)
            )

            list.removeAt(pos)
            adapter.notifyItemRemoved(pos)
            adapter.notifyItemRangeChanged(pos, list.size)
            dialog.dismiss()
        }

        dialogBinding.btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog = dialogBuilder.create()
        dialog.show()

        Toast.makeText(requireActivity(), "${list.size}", Toast.LENGTH_SHORT).show()
    }

    private fun addEventClickItem(pos: Int) {
        val i = Intent(requireActivity(), InsertScheduleActivity::class.java)
        i.putExtra("flag", true)
        i.putExtra("_id", list[pos]._id)
        i.putExtra("day", list[pos].day)
        i.putExtra("title", list[pos].title)
        i.putExtra("fullday", list[pos].fullday)
        i.putExtra("timestart", list[pos].timeStart)
        i.putExtra("timeend", list[pos].timeEnd)
        i.putExtra("repeat", list[pos].repeat)
        i.putExtra("remind", list[pos].remind)
        i.putExtra("place", list[pos].place)
        i.putExtra("notes", list[pos].notes)
        startActivity(i)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        getValueDataBase()
        if (list.isEmpty()) {
            binding.txtNotification.visibility = View.VISIBLE
            binding.rvListSchedule.visibility = View.GONE
        } else {
            binding.txtNotification.visibility = View.GONE
            binding.rvListSchedule.visibility = View.VISIBLE
        }
        setUpAdapter()
    }
}