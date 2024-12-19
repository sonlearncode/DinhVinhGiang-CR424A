package com.example.to_do_list.controller

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.to_do_list.R
import com.example.to_do_list.data.MyHelper
import com.example.to_do_list.databinding.ActivityScheduleBinding
import com.example.to_do_list.databinding.CustomDialogConfirmBinding
import java.util.Calendar

class InsertScheduleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScheduleBinding
    private lateinit var db: SQLiteDatabase
    private var flag = false
    private var scheduleID: String? = null
    private val toDay = Calendar.getInstance()
    private var daySchedule: String? = null
    private lateinit var dialog: AlertDialog
    private val repeat = listOf("One time", "Twice", "Three times", "Four", "Five times")
    private val remind = listOf("Before 5 minutes", "Before 10 minutes", "Before 15 minutes")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val helper = MyHelper(this)
        db = helper.readableDatabase

        setUpSpinner()

        addEventBtnBack()
        if (flag) {
            getIntentPutExtras()
        }

        addEventBtnDateTimePicker()
        addEventBtnInsertSchedule()
        getIntentPutExtras()
      
    }

    private fun setUpSpinner() {
        val adtsp1 =
            object : ArrayAdapter<String>(this, android.R.layout.simple_gallery_item, repeat) {
                @SuppressLint("ResourceAsColor")
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    val textView = view as TextView
                    textView.setTextColor(
                        ContextCompat.getColor(
                            this@InsertScheduleActivity,
                            R.color.btn_pickDate
                        )
                    )
                    return view
                }
            }
        adtsp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spRepeat.adapter = adtsp1

        val adtsp2 =
            object : ArrayAdapter<String>(this, android.R.layout.simple_gallery_item, remind) {
                @SuppressLint("ResourceAsColor")
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    val textView = view as TextView
                    textView.setTextColor(
                        ContextCompat.getColor(
                            this@InsertScheduleActivity,
                            R.color.btn_pickDate
                        )
                    )
                    return view
                }
            }
        adtsp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spRemind.adapter = adtsp2
    }

    private fun addEventBtnInsertSchedule() {
        binding.btnInsertSchedule.setOnClickListener {
            val i = Intent(this, Schedule_fragment::class.java)
            val d = binding.txtStart.text.toString().split("-")
            daySchedule = d[0]
            val day = daySchedule
            val title = binding.edtTitle.text.toString()
            var fullday: Int = 0
            if (binding.swtFullday.isChecked) {
                fullday = 1
            }
            val timestart = binding.txtStart.text.toString()
            val timeend = binding.txtFinish.text.toString()
            val place = binding.edtPlace.text.toString()
            val notes = binding.edtNotes.text.toString()

            if (day == "" || title == "" || timestart == "" || timeend == "" || place == "" || notes == "") {
                Toast.makeText(this, "Hãy điền đầy đủ nội dung của schedule!!!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val dialogBuilder = AlertDialog.Builder(this)
                val dialogBinding = CustomDialogConfirmBinding.inflate(LayoutInflater.from(this))
                dialogBuilder.setView(dialogBinding.root)

                dialogBinding.txtContent.text = "Do you wanna create a schedule?"
                dialogBinding.btnYes.setOnClickListener {
                    val query =
                        "INSERT INTO SCHEDULE(DAY, TITLE, FULLDAY, TIMESTART, TIMEEND, PLACE, NOTES) VALUES(?, ?, ?, ?, ?, ? ,?)"
                    db.execSQL(
                        query,
                        arrayOf(day, title, fullday, timestart, timeend, place, notes)
                    )
                    finish()
                }

                dialogBinding.btnNo.setOnClickListener {
                    dialog.dismiss()
                }

                dialog = dialogBuilder.create()
                dialog.show()
            }
        }
    }

    //mai phải hoàn thành sự kiện chọn time start và end
    private fun addEventBtnDateTimePicker() {
        binding.btnPickDateStart.setOnClickListener {
            datePicker(binding.txtStart)
        }

        binding.btnPickDateFinish.setOnClickListener {
            datePicker(binding.txtFinish)
        }
    }

    private fun datePicker(txt: TextView) {
        val day = toDay.get(Calendar.DAY_OF_MONTH)
        val month = toDay.get(Calendar.MONTH)
        val year = toDay.get(Calendar.YEAR)
        val hour = toDay.get(Calendar.HOUR_OF_DAY)
        val minute = toDay.get(Calendar.MINUTE)

        //pick date
        DatePickerDialog(
            this, DatePickerDialog.OnDateSetListener { _, i, i2, i3 ->
                var s = "$i3-${i2 + 1}-$i"

                //pick time
                TimePickerDialog(
                    this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                        val amPm = if (hourOfDay < 12) "a.m" else "p.m"
                        s += " $hourOfDay:$minute($amPm)"
                        txt.text = s
                    },
                    hour, minute, true
                ).show()

            }, year, month, day
        ).show()
    }

    //get value được truyền từ activity trước
    private fun getIntentPutExtras() {
        flag = intent.getBooleanExtra("flag", false)
        scheduleID = intent.getStringExtra("_id")
        daySchedule = intent.getStringExtra("day")
        val title = intent.getStringExtra("title")
        val fullDay = intent.getIntExtra("fullday", 0)
        val timeStart = intent.getStringExtra("timestart")
        val timeEnd = intent.getStringExtra("timeend")
        val repeat = intent.getIntExtra("repeat", 0)
        val remind = intent.getIntExtra("remind", 0)
        val place = intent.getStringExtra("place")
        val notes = intent.getStringExtra("notes")

        binding.edtTitle.setText(title)
        if (fullDay == 1) {
            binding.swtFullday.isChecked = true
        }
        binding.txtStart.text = timeStart
        binding.txtFinish.text = timeEnd
        binding.spRepeat.setSelection(repeat)
        binding.spRemind.setSelection(remind)
        binding.edtPlace.setText(place)
        binding.edtNotes.setText(notes)

    }

    private fun addEventBtnBack() {
        binding.btnBack.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val dialogBinding = CustomDialogConfirmBinding.inflate(LayoutInflater.from(this))
            dialogBuilder.setView(dialogBinding.root)

            dialogBinding.btnYes.setOnClickListener {
                finish()
            }

            dialogBinding.btnNo.setOnClickListener {
                dialog.dismiss()
            }

            dialog = dialogBuilder.create()
            dialog.show()
        }
    }

    override fun onPause() {
        super.onPause()
        updateScheduleInDatabase()
    }

    private fun updateScheduleInDatabase() {
        if (flag) {
            val d = binding.txtStart.text.toString().split("-")
            daySchedule = d[0]
            val title = binding.edtTitle.text.toString()
            val fullday = binding.swtFullday.isChecked
            val start = binding.txtStart.text.toString()
            val finish = binding.txtFinish.text.toString()
            val repeat = binding.spRepeat.selectedItemPosition
            val remind = binding.spRemind.selectedItemPosition
            val place = binding.edtPlace.text.toString()
            val notes = binding.edtNotes.text.toString()

            val contentValue = ContentValues().apply {
                put("DAY", daySchedule)
                put("TITLE", title)
                put("FULLDAY", fullday)
                put("TIMESTART", start)
                put("TIMEEND", finish)
                put("REPEAT", repeat)
                put("REMINDER", remind)
                put("PLACE", place)
                put("NOTES", notes)
            }

            db.update("SCHEDULE", contentValue, "_id = ?", arrayOf(scheduleID))
        }

    }
}