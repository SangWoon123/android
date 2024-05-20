package com.example.myapplication.worldcup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityPopupBinding

class PopupActivity : AppCompatActivity() {
    lateinit var binding: ActivityPopupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityPopupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageResourceId = intent.getIntExtra("image", 0) // 기본값으로 0을 넣어줍니다.
        val menuName = intent.getStringExtra("name")

        binding.imageText.setText("오늘의 저녁메뉴 $menuName 어떠세요?")
        if (imageResourceId != 0) { // 유효한 이미지 리소스 ID인 경우에만 설정
            binding.resultImage.setImageResource(imageResourceId)
        }


        // '추천 받으러 가기' 버튼 클릭 이벤트 처리
        binding.goButton.setOnClickListener {
            // 여기에 추천 받으러 가는 로직을 구현하세요.
            showNearbyRestaurants(menuName.toString())
            // 예: Intent를 사용하여 다른 액티비티로 이동
            // Intent(this, RecommendationActivity::class.java).also {
            //     startActivity(it)
            // }
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

            finish()
        }

        binding.reset.setOnClickListener {
            val intent = Intent(this, WorldCup16::class.java)
            // 기존에 존재하는 액티비티 스택을 정리하기 위한 플래그 설정
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }


    }


    private fun showNearbyRestaurants(menuName: String) {
        // 인텐트를 사용하여 웹 브라우저를 열고 네이버 검색 결과를 표시합니다.
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://search.naver.com/search.naver?query=주변 $menuName")
        )
        startActivity(intent)
    }


}