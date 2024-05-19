package com.example.myapplication.Memo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.DataBase.LocalDatabase
import com.example.myapplication.DataBase.MemoEntity
import com.example.myapplication.databinding.ActivityMemoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//리뷰 작성
class MemoActivity : AppCompatActivity(){
    private lateinit var db : LocalDatabase
    lateinit var binding: ActivityMemoBinding
    var mystar:Int=0
    var date: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = LocalDatabase.getInstance(applicationContext)!!
        date =intent.getStringExtra("date")
        binding.date.text = date
        Log.d("knh", date.toString())

        binding.save.setOnClickListener {
            addMemo()
            val toast = Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT)
            toast.show()
            //DB저장 date,title,content,emotion
            startActivity(Intent(this, MemoListActivity::class.java))
        }

        //평점 입력칸 추가
        binding.star.run{
            //rating = 설정된 별점 값
            // fromUser = 사용자에 의해 설정되었는지 여부
            setOnRatingBarChangeListener{ star, rating, fromUser ->
                mystar = rating.toInt()
                //Log.d("knh",mystar.toString())
            }
        }

    }

    private fun addMemo(){
        val DBtitle = binding.title.text.toString()
        val DBcontent = binding.content.text.toString()
        val DBdate = binding.date.text.toString()
        val DBstar = mystar //평점 추가

        CoroutineScope(Dispatchers.IO).launch {
            db.getMemoDao().insertMemo(MemoEntity(DBdate,DBtitle,DBcontent,DBstar))
            //아직 평점은 안넣음
            //2024.05.17 평점 추가
        }
    }


}