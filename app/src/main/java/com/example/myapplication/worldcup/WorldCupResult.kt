package com.example.myapplication.worldcup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityWorldCupResultBinding

class WorldCupResult : AppCompatActivity() {
    lateinit var binding: ActivityWorldCupResultBinding
    private var menu = mutableListOf<Menu>() // 선택된 메뉴들을 저장할 리스트

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorldCupResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "저녁 메뉴 추천 월드컵"

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

    // 팝업창과 함께 선택된 최종메뉴를 보여준다.
    private fun onMenuSelected(selectMenu: Menu) {

        intent = Intent(this, PopupActivity::class.java)
        intent.putExtra("image", selectMenu.imageResourceId)
        intent.putExtra("name", selectMenu.name)
        startActivity(intent)

    }


}