package com.example.myapplication

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.myapplication.DataBase.LocalDatabase
import com.example.myapplication.Memo.MemoListActivity
import com.example.myapplication.Personal.PersonalActivity
import com.example.myapplication.Rullet.RulletActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.worldcup.WorldCupReady
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

        title = "오늘의 밥 메뉴 추천은?"


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
        binding.worldcup.setOnClickListener {
            //월드컵 이동
            startActivity(Intent(this,WorldCupReady::class.java))
        }

        binding.menuWheel.setOnClickListener {
            //돌림판 이동
        }
        binding.wheel.setOnClickListener {
            //돌림판 이동
            startActivity(Intent(this,RulletActivity::class.java))
        }

        binding.menuMemo.setOnClickListener {
            startActivity(Intent(this,MemoListActivity::class.java))
        }
        binding.memo.setOnClickListener {
            startActivity(Intent(this,MemoListActivity::class.java))
        }


        binding.tipMenu.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://ibook.tukorea.ac.kr/Viewer/menu02"))
            startActivity(intent)
        }
        binding.eMenu.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://ibook.tukorea.ac.kr/Viewer/menu01"))
            startActivity(intent)
        }
        binding.map.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }
        binding.random.setOnClickListener {
            //랜덤 뽑기 화면 이동
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