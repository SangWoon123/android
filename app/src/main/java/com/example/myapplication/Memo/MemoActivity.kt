package com.example.myapplication.Memo

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = LocalDatabase.getInstance(applicationContext)!!

        binding.save.setOnClickListener {
            addMemo()
            val toast = Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT)
            toast.show()
            //DB저장 date,title,content,emotion
            finish()
        }

        //평점 입력칸 추가

    }

    private fun addMemo(){
        val DBtitle = binding.title.text.toString()
        val DBcontent = binding.content.text.toString()
        val DBdate = binding.date.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            db.getMemoDao().insertMemo(MemoEntity(DBtitle,DBcontent,DBdate))
            //아직 평점은 안넣음
        }
    }


}