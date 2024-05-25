package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityRandomdrawBinding
import com.example.myapplication.worldcup.Menu
import kotlin.random.Random

class RandomdrawActivity  : AppCompatActivity() {
    private lateinit var menuManager: MenuManager
    lateinit var binding: ActivityRandomdrawBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRandomdrawBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "랜덤 뽑기"

        // 랜덤 인덱스
        val randomInt = Random.nextInt(1, 17)
        // 메뉴 리스트 생성
        menuManager = MenuManager()
        // 현재 메뉴 리스트 복사
        val menus = menuManager.initialMenus.toMutableList()
        // 랜덤 인덱스에 위치한 메뉴 선택
        val randomFood = menus[randomInt]

        binding.imageView.setImageResource(randomFood.imageResourceId)
        binding.foodName.text = randomFood.name
    }

    class MenuManager {
        val initialMenus = mutableListOf<Menu>()

        init {
            // 초기 메뉴 데이터 설정
            initializeMenuData()
        }

        private fun initializeMenuData() {
            initialMenus.add(Menu("피자", R.drawable.pizza))
            initialMenus.add(Menu("파스타", R.drawable.pasta))
            initialMenus.add(Menu("스테이크", R.drawable.stake))
            initialMenus.add(Menu("김밥", R.drawable.kimbab))
            initialMenus.add(Menu("샐러드", R.drawable.salad))
            initialMenus.add(Menu("치킨", R.drawable.chicken))
            initialMenus.add(Menu("라면", R.drawable.ramen))
            initialMenus.add(Menu("짜장면", R.drawable.zzazang))
            //
            initialMenus.add(Menu("국밥", R.drawable.kokbab))
            initialMenus.add(Menu("햄버거", R.drawable.hamberg))
            initialMenus.add(Menu("냉면", R.drawable.nang))
            initialMenus.add(Menu("돈까스", R.drawable.don))
            initialMenus.add(Menu("보쌈", R.drawable.bossam))
            initialMenus.add(Menu("마라탕", R.drawable.mara))
            initialMenus.add(Menu("부대찌개", R.drawable.budae))
            initialMenus.add(Menu("삼겹살", R.drawable.sam))
        }
    }
}