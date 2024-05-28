package com.example.myapplication.worldcup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityWorldCup16Binding


class WorldCup16 : AppCompatActivity() {
    private lateinit var menuManager: MenuManager
    lateinit var binding: ActivityWorldCup16Binding
    val selectedMenus = ArrayList<Menu>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorldCup16Binding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "밥메뉴 추천 월드컵"
        menuManager = MenuManager()
        setupUI()
    }

    private fun setupUI() {
        menuManager.resetCurrentMenus()
        showTwoMenus()
    }

    private fun showTwoMenus() {
        val menus = menuManager.currentMenus.toMutableList() // 원본 리스트 복사
        if (menus.size >= 2) {
            val pickedIndexes = (menus.indices).shuffled().take(2)

            val menu1 = menus[pickedIndexes[0]]
            val menu2 = menus[pickedIndexes[1]]

            menuSelect(menu1, menu2)

            if (pickedIndexes[0] > pickedIndexes[1]) {
                menus.removeAt(pickedIndexes[0])
                menus.removeAt(pickedIndexes[1])
            } else {
                menus.removeAt(pickedIndexes[1])
                menus.removeAt(pickedIndexes[0])
            }

            menuManager.currentMenus = menus // 수정된 리스트를 현재 메뉴로 업데이트

        }

        Log.d("kkang 선택후", menus.size.toString())
    }

    private fun menuSelect(menu1: Menu, menu2: Menu) {
        binding.buttonChooseMenu1.apply {
            text = menu1.name
            setOnClickListener {
                onMenuSelected(menu1)
            }
        }

        binding.buttonChooseMenu2.apply {
            text = menu2.name
            setOnClickListener {
                onMenuSelected(menu2)
            }
        }

        binding.imageViewMenu1.setImageResource(menu1.imageResourceId)
        binding.imageViewMenu2.setImageResource(menu2.imageResourceId)
    }


    // 메뉴 선택 후 결과 처리
    private fun onMenuSelected(selectedMenu: Menu) {
        // 선택된 메뉴를 result에 추가하는 로직
        selectedMenus.add(selectedMenu)

        if (selectedMenus.size == 8) {
            navigateToWorldCup8()
        } else {
            showTwoMenus()
        }
    }


    private fun navigateToWorldCup8() {
        val intent = Intent(this, WorldCup8::class.java)
        intent.putExtra("selectedMenus", selectedMenus)
        startActivity(intent)
    }


    class MenuManager {
        private val initialMenus = mutableListOf<Menu>()
        var currentMenus = mutableListOf<Menu>()

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

        fun resetCurrentMenus() {
            currentMenus.clear()
            currentMenus.addAll(initialMenus)
        }
    }

}