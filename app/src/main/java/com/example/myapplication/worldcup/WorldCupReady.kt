package com.example.myapplication.worldcup

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityWorldCupReadyBinding

class WorldCupReady : AppCompatActivity() {
    lateinit var binding: ActivityWorldCupReadyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorldCupReadyBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.startButton.setOnClickListener {
            startActivity(Intent(this, WorldCup16::class.java))
        }


    }

}