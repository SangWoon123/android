package com.example.myapplication.Personal

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.myapplication.databinding.ImgdialogConfrimBinding


//다이얼로그 : imgdialog 부분.
class PersonalDialog : DialogFragment(){
    private var bindingg: ImgdialogConfrimBinding? = null
    private val binding get() = bindingg!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingg = ImgdialogConfrimBinding.inflate(inflater, container, false)
        val view = binding.root
        val intent = Intent(requireContext(), PersonalActivity::class.java)

        //레이아웃 배경을 투명하게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        isCancelable = false

        binding.GelleryBtn.setOnClickListener {
            //갤러리 접근
            val intent = Intent(requireContext(), PersonalActivity::class.java)
            intent.putExtra("conf", "G")
            startActivity(intent)
            //Log.d("knh","갤러리 버튼 클릭")
        }
        binding.CameraBtn.setOnClickListener {
            //카메라 접근
            val intent = Intent(requireContext(), PersonalActivity::class.java)
            intent.putExtra("conf", "C")
            startActivity(intent)
            //Log.d("knh","카메라 버튼 클릭")
        }

        return view
    }

}