package com.example.myapplication.worldcup

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityWorldCupReadyBinding
import android.view.Menu
import android.view.MenuItem

class WorldCupReady : AppCompatActivity() {
    lateinit var binding: ActivityWorldCupReadyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorldCupReadyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title="저녁 메뉴 월드 추천 월드컵"


        binding.startButton.setOnClickListener {
            startActivity(Intent(this, WorldCup16::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.total_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.help-> {
                startActivity(Intent(this, WorldCupHelpActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

}