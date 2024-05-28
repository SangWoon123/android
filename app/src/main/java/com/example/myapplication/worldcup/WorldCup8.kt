package com.example.myapplication.worldcup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityWorldCup8Binding

class WorldCup8 : AppCompatActivity() {
    lateinit var binding: ActivityWorldCup8Binding
    private var menu = mutableListOf<Menu>() // 선택된 메뉴들을 저장할 리스트
    private var selectedMenus: MutableList<Menu> = mutableListOf() // 수정됨

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorldCup8Binding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "밥메뉴 추천 월드컵"

        // Intent로부터 전달받은 선택된 메뉴 리스트를 가져옵니다.
        intent.getParcelableArrayListExtra<Menu>("selectedMenus")?.let {
            menu = it.toMutableList()
        }


        // 화면에 메뉴 보여주기 시작
        showTwoMenus()
    }

    private fun showTwoMenus() {
        if (menu.size >= 2) {
            // 랜덤하게 두 개의 인덱스를 선택합니다.
            val pickedIndexes = (menu.indices).shuffled().take(2)

            val menu1 = menu[pickedIndexes[0]]
            val menu2 = menu[pickedIndexes[1]]

            // 메뉴 선택 UI 업데이트
            menuSelect(menu1, menu2)

            // 선택된 메뉴를 리스트에서 제거합니다.
            if (pickedIndexes[0] > pickedIndexes[1]) {
                menu.removeAt(pickedIndexes[0])
                menu.removeAt(pickedIndexes[1])
            } else {
                menu.removeAt(pickedIndexes[1])
                menu.removeAt(pickedIndexes[0])
            }

        }
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
    private fun onMenuSelected(selectMenu: Menu) {
        // 선택된 메뉴를 result에 추가하는 로직
        selectedMenus.add(selectMenu)

        if (selectedMenus.size == 4) {
            navigateToWorldCup4()
        } else {
            showTwoMenus()
        }
    }

    private fun navigateToWorldCup4() {
        val intent = Intent(this, WorldCup4::class.java)
        intent.putParcelableArrayListExtra("selectedMenus", ArrayList(selectedMenus))
        startActivity(intent)
    }
}