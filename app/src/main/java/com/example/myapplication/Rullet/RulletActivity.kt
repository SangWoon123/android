package com.example.myapplication.Rullet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.myapplication.R
import com.example.myapplication.RulletHelpActivity
import com.example.myapplication.databinding.ActivityRulletBinding

class RulletActivity : AppCompatActivity() {

    lateinit var binding: ActivityRulletBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRulletBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "돌려돌려 돌림판"

        binding.finishBtn.setOnClickListener{
            val intent = Intent(this, RulletShowActivity::class.java)
            intent.putExtra("text1", binding.text1.text.toString())
            intent.putExtra("text2", binding.text2.text.toString())
            intent.putExtra("text3", binding.text3.text.toString())
            intent.putExtra("text4", binding.text4.text.toString())
            intent.putExtra("text5", binding.text5.text.toString())
            intent.putExtra("text6", binding.text6.text.toString())
            setResult(RESULT_OK, intent)
            startActivity(intent)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.total_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.help-> {
                startActivity(Intent(this, RulletHelpActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

}