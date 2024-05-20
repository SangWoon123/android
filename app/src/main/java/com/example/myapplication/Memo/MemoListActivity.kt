package com.example.myapplication.Memo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DataBase.LocalDatabase
import com.example.myapplication.databinding.ActivityMemolistBinding
import com.example.myapplication.databinding.ItemMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//리뷰 리스트 출력
class MemoListActivity : AppCompatActivity(){

    private lateinit var db : LocalDatabase
    lateinit var binding: ActivityMemolistBinding
    var memo:List<String>? = null
    var size:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemolistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = LocalDatabase.getInstance(applicationContext)!!
        val intent =Intent(this,CalenderActivity::class.java)

        title = "평점 기록 리스트"

        CoroutineScope(Dispatchers.IO).launch {
            //메모 제목 받아옴
            val mutableMemo = db.getMemoDao().getSetMemo().toMutableList()
            size = mutableMemo.size //메모 개수

            withContext(Dispatchers.Main){
                val adapter = MyAdapter(mutableMemo) //변경 가능한 리스트로 어댑터 생성
                binding.memo.layoutManager = LinearLayoutManager(this@MemoListActivity)
                binding.memo.adapter = adapter
                //Log.d("knh_data", mutableMemo.toString())
            }
        }

        binding.addBtn.setOnClickListener {
            intent.putExtra("request","memo") //캘린더로 이동
            startActivity(intent)
        }


    }
}
class MyViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)

class MyAdapter(val datas:MutableList<String>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyViewHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context),parent,false))


    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        binding.itemTitle.text = datas[position]
        binding.itemTitle.setOnClickListener { //아이템 클릭하면?
            val context = binding.root.context
            val intent = Intent(context, ShowActivity::class.java)
            intent.putExtra("title", datas[position]) // 현재 아이템의 데이터 전달
            context.startActivity(intent)
        }
    }
}