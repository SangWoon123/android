package com.example.myapplication.Memo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.DataBase.LocalDatabase
import com.example.myapplication.databinding.ActivityShowBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShowActivity : AppCompatActivity() {

    lateinit var binding : ActivityShowBinding
    private lateinit var db : LocalDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title="평점 기록"

        db = LocalDatabase.getInstance(applicationContext)!!

        //이전 화면에서 전달된 제목 가져오기
        val memoTitle = intent.getStringExtra("title").toString()

        CoroutineScope(Dispatchers.IO).launch {
            // 고유 아이디를 사용하여 메모의 상세 내용 가져오기
            val memoDetail = db.getMemoDao().getMemoByTitle(memoTitle)

            // UI 업데이트는 Main 스레드에서 수행되어야 함
            withContext(Dispatchers.Main) {
                // 가져온 메모 상세 내용을 TextView에 설정
                binding.date.text = memoDetail?.date
                binding.title.text = memoDetail?.title
                binding.content.text = memoDetail?.content ?: "메모 내용이 없습니다."
                binding.star.rating = memoDetail?.star?.toFloat() ?: 0.0f
            }
        }


        binding.delete.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                // 고유 아이디를 사용하여 메모의 상세 내용 가져오기
                val memo = db.getMemoDao().getMemoByTitle(memoTitle)
                memo?.let {
                    // 메모를 삭제하는 쿼리 실행
                    db.getMemoDao().deleteMemoById(memo)
                }
                // 삭제가 완료되면 현재 액티비티 종료
                withContext(Dispatchers.Main) {
                    val toast = Toast.makeText(applicationContext, "삭제되었습니다", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
        }


    }
}