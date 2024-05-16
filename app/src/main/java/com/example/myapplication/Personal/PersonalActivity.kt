package com.example.myapplication.Personal

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.myapplication.DataBase.LocalDatabase
import com.example.myapplication.DataBase.UserEntity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityPersonalBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class PersonalActivity : AppCompatActivity(){
    lateinit var binding : ActivityPersonalBinding
    private lateinit var db : LocalDatabase
    var filePath : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = LocalDatabase.getInstance(applicationContext)!!
        title = "마이페이지"

        //이미지 넣기
        setImage()

        //유저정보 추가
        binding.addButton.setOnClickListener {
            if(filePath!=""){ //안비어있다면? 사진이 있을시.
                val file = File(filePath.toString())
                val byteArray = file.readBytes()
                Log.d("knh", byteArray.toString()) //되는지 확인하게 로그 찍어뵉
                addUserWithImage(byteArray)
                //안내 메시지
                val toast = Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT)
                toast.show()
            }
            else{
                addUser()
                //안내 메시지
                val toast = Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

        //취소
        binding.cancleBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        //이미지추가
        binding.userImageBtn.setOnClickListener {
            PersonalDialog().show(supportFragmentManager, "dialog")
        }

        //gallery request launcher.......
        val requestGallerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            try {
                val calRatio = calculateInSampleSize(
                    it.data!!.data!!,
                    resources.getDimensionPixelSize(R.dimen.imgSize),
                    resources.getDimensionPixelSize(R.dimen.imgSize)
                )
                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio

                var inputStream = contentResolver.openInputStream(it.data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
                inputStream!!.close()
                inputStream = null

                bitmap?.let{
                    binding.userImageBtn.setImageBitmap(bitmap)
                } ?: let{
                    Log.d("knh","bitmap null")
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

        val requestCamreaFileLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            val calRatio = calculateInSampleSize(
                Uri.fromFile(File(filePath)),
                resources.getDimensionPixelSize(R.dimen.imgSize),
                resources.getDimensionPixelSize(R.dimen.imgSize)
            )
            val option = BitmapFactory.Options()
            option.inSampleSize = calRatio
            val bitmap = BitmapFactory.decodeFile(filePath, option)
            bitmap?.let{
                binding.userImageBtn.setImageBitmap(bitmap)
            }
        }


        val conf = intent.getStringExtra("conf")
        if(conf!=null){
            if(conf=="G"){ //와 미친 이게 되네
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                requestGallerLauncher.launch(intent)
                Log.d("knh","갤러리 선택")
            }
            else if(conf=="C"){
                val timeStamp: String =
                    SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val  storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                val file = File.createTempFile(
                    "JPEG_${timeStamp}_",
                    ".jpg",
                    storageDir
                )
                filePath = file.absolutePath
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "com.example.myapplication.fileprovider",
                    file
                )
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                requestCamreaFileLauncher.launch(intent)
                Log.d("knh","카메라 선택")
            }
        }

    }


    //필요한 함수정의 -- userTable관련
    private fun setImage(){
        CoroutineScope(Dispatchers.IO).launch {
            val imageByteArray = db.getUserDao().getImage()
            imageByteArray?.let {
                // 이미지 바이트 배열을 비트맵으로 변환하여 ImageView에 표시
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                runOnUiThread {
                    if(bitmap != null){
                        binding.userImageBtn.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }


    //유저 정보 저장
    private fun addUser() {
        val username = binding.name.text.toString()
        val userage = binding.age.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            db.getUserDao().insertUser(UserEntity(username, userage))
        }
    }


    //사진과 정보 저장
    private fun addUserWithImage(imageByteArray: ByteArray) {
        val username = binding.name.text.toString()
        val userage = binding.age.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            db.getUserDao().insertUser(UserEntity(username, userage, imageByteArray))
        }
    }

    //그림 크기 자르기
    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try {
            var inputStream = contentResolver.openInputStream(fileUri)

            //inJustDecodeBounds 값을 true 로 설정한 상태에서 decodeXXX() 를 호출.
            //로딩 하고자 하는 이미지의 각종 정보가 options 에 설정 된다.
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
            inputStream = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //비율 계산........................
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1
        //inSampleSize 비율 계산
        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}