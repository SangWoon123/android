package com.example.myapplication.Rullet


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.bluehomestudio.luckywheel.LuckyWheel
import com.bluehomestudio.luckywheel.OnLuckyWheelReachTheTarget
import com.bluehomestudio.luckywheel.WheelItem
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityRulletShowBinding
import java.util.Random

class RulletShowActivity : AppCompatActivity() {

    lateinit var binding:ActivityRulletShowBinding
    private lateinit var luckyWheel: LuckyWheel
    private lateinit var wheelItems: List<WheelItem>
    private var point: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRulletShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "돌림판"

        // 변수에 담기
        luckyWheel = findViewById(R.id.luck_wheel)

        val item1 = intent.getStringExtra("text1") //inent 받음
        val item2 = intent.getStringExtra("text2")
        val item3 = intent.getStringExtra("text3")
        val item4 = intent.getStringExtra("text4")
        val item5 = intent.getStringExtra("text5")
        val item6 = intent.getStringExtra("text6")

        // 점수판 데이터 생성
        generateWheelItems(item1,item2,item3,item4,item5,item6)

        // 점수판 타겟 정해지면 이벤트 발생
        luckyWheel.setLuckyWheelReachTheTarget(object : OnLuckyWheelReachTheTarget {
            override fun onReachTarget() {
                // 아이템 변수에 담기
                val wheelItem = wheelItems[point!!.toInt() - 1]

                // 아이템 텍스트 변수에 담기
                val money = wheelItem.text  //결과.

                // 메시지
                //Toast.makeText(this@RulletShowActivity, money, Toast.LENGTH_SHORT).show()
                RulletDialog(money).show(supportFragmentManager, "dialog")
                
            }
        })

        // 시작버튼
        val start = findViewById<Button>(R.id.spin_btn)
        start.setOnClickListener {
            val random = Random()
            point = (random.nextInt(6) + 1).toString() // 1 ~ 6
            luckyWheel.rotateWheelTo(point!!.toInt())
        }
    }

    /**
     * 데이터 담기
     */
    private fun Any.generateWheelItems(item1:String?, item2: String?, item3: String?, item4: String?, item5: String?, item6: String?) {
        wheelItems = ArrayList()

        val d = resources.getDrawable(R.drawable.ic_launcher_foreground, null)

        val bitmap = drawableToBitmap(d)

        wheelItems += WheelItem(Color.parseColor("#FFD700"), bitmap, item1)
        wheelItems += WheelItem(Color.parseColor("#E91E63"), bitmap, item2)
        wheelItems += WheelItem(Color.parseColor("#7CFC00"), bitmap, item3)
        wheelItems += WheelItem(Color.parseColor("#ADFF2F"), bitmap, item4)
        wheelItems += WheelItem(Color.parseColor("#FF8C00"), bitmap, item5)
        wheelItems += WheelItem(Color.parseColor("#FAFAD2"), bitmap, item6)

        // 점수판에 데이터 넣기
        luckyWheel.addWheelItems(wheelItems)
    }

    /**
     * drawable -> bitmap
     * @param drawable drawable
     * @return
     */
    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(0, 0, 0, "help")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            0 -> {
                //startActivity(Intent(this, RulletHelpActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}