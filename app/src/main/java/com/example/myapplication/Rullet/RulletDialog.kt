package com.example.myapplication.Rullet

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityRulletDialogBinding

class RulletDialog(result: String) : DialogFragment() {

    private var bindingg :ActivityRulletDialogBinding? = null
    private val binding get() = bindingg!!
    private var result: String? =null

    init {
        this.result = result
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingg = ActivityRulletDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        val intent = Intent(requireContext(), RulletActivity::class.java)

        binding.result.text = result

        //레이아웃 배경을 투명하게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        isCancelable = false

        binding.confirm.setOnClickListener {
            //다시 돌아가기
            startActivity(intent)
        }

        return view
    }
}