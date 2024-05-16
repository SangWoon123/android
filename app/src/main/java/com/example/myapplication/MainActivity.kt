package com.example.myapplication

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.myapplication.DataBase.LocalDatabase
import com.example.myapplication.Memo.MemoListActivity
import com.example.myapplication.Personal.PersonalActivity
import com.example.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var binding : ActivityMainBinding
    private lateinit var db : LocalDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = LocalDatabase.getInstance(applicationContext)!!


        //메인화면 내비게이션에 DB에 저장된 그림과 이름 넣기
        setImage()
        setName()

        //내비게이션 바 생성
        toggle = ActionBarDrawerToggle(this, binding.drawerMain, R.string.drawer_opened, R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()


        binding.menuPersonal.setOnClickListener {
            //마이페이지 이동
            startActivity(Intent(this, PersonalActivity::class.java))
        }
        binding.menuWorldcup.setOnClickListener {
            //월드컵 이동
        }
        binding.menuWheel.setOnClickListener {
            //돌림판 이동
        }
        binding.menuMemo.setOnClickListener {
            startActivity(Intent(this,MemoListActivity::class.java))
        }


    }

    //유저 닉네임 넣기
    private fun setName() {
        CoroutineScope(Dispatchers.IO).launch {
            val latestUserName = db.getUserDao().getUser()
            runOnUiThread {
                binding.username.text = latestUserName ?: "user" // 최신 사용자 이름이 없는 경우 기본 이름 설정
            }
        }
    }

    //유저 이미지 넣기
    private fun setImage() {
        CoroutineScope(Dispatchers.IO).launch {
            val imageByteArray = db.getUserDao().getImage()
            imageByteArray?.let {
                // 이미지 바이트 배열을 비트맵으로 변환하여 ImageView에 표시
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                runOnUiThread {
                    if (bitmap != null) {
                        binding.userImage.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }



}