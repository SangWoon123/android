package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityRandomdrawBinding

class RandomdrawActivity : AppCompatActivity() {

    lateinit var binding: ActivityRandomdrawBinding
    // 아무거나 뽑아서 보여주는 화면.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRandomdrawBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}