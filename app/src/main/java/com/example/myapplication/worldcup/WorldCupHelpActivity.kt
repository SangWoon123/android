package com.example.myapplication.worldcup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityWorldCupHelpBinding

class WorldCupHelpActivity : AppCompatActivity() {
    lateinit var binding: ActivityWorldCupHelpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWorldCupHelpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "월드컵 설명"
    }
}