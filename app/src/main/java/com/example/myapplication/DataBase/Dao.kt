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
    fun insertDiary(memo: MemoEntity)

    @Query("SELECT * FROM memo") // 테이블의 모든 값을 가져와라
    fun getAllDiary(): List<MemoEntity>

    // 제목을 기반으로 메모 가져오는 쿼리
    @Query("SELECT * FROM memo WHERE title = :memoTitle")
    suspend fun getDiaryByTitle(memoTitle: String): MemoEntity?

    @Query("SELECT title FROM memo")
    fun getSetDiary(): List<String>

    @Update
    fun updateDiary(memo: MemoEntity)

    // 제목을 기반으로 메모 삭제하는 쿼리
    @Delete
    suspend fun deleteDiaryById(memo: MemoEntity)
}