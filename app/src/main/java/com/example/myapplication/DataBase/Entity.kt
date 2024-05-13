package com.example.myapplication.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("user")
data class UserEntity(
    val name : String,
    val age : String,
    var Image : ByteArray? = null
){
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}

@Entity("memo")
data class MemoEntity(
    val date : String,
    val title:String,
    val content:String?,
    val star:Int,
){
    @PrimaryKey(autoGenerate = true) var id:Int=0
}
