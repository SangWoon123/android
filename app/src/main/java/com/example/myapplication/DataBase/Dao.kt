package com.example.myapplication.DataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: UserEntity)

    @Query("Select name FROM user ORDER BY id DESC LIMIT 1")
    fun getUser(): String

    @Query("SELECT image FROM user ORDER BY id DESC LIMIT 1")
    fun getImage(): ByteArray
}

@Dao
interface MemoDao{
    @Insert
    fun insertMemo(memo: MemoEntity)

    // 제목을 기반으로 메모 가져오는 쿼리
    @Query("SELECT * FROM memo WHERE title = :memoTitle")
    suspend fun getMemoByTitle(memoTitle: String): MemoEntity?

    @Query("SELECT title FROM memo")
    fun getSetMemo(): List<String>

    @Query("SELECT id FROM memo")
    fun getMemoID(): List<Int>

    @Update
    fun updateMemo(memo: MemoEntity)

    // 제목을 기반으로 메모 삭제하는 쿼리
    @Delete
    suspend fun deleteMemoById(memo: MemoEntity)
}