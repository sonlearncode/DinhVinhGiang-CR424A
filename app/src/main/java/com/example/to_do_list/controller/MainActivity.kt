package com.example.to_do_list.controller

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.to_do_list.databinding.ActivityMainBinding

//test commit cmt
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AddEventStart()
    }

    private fun AddEventStart() {
        binding.btnStart.setOnClickListener {
            val i = Intent(this, MakeNoteActivity::class.java)
            startActivity(i)
        }
    }
}