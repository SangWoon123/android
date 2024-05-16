package com.example.myapplication.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [UserEntity::class, MemoEntity::class], version = 1, exportSchema = false
)
@TypeConverters(RoomTypeConvert::class)
abstract class LocalDatabase : RoomDatabase(){
    abstract fun getUserDao() : UserDao
    abstract fun getMemoDao() : MemoDao

    companion object{
        private var instance: LocalDatabase? = null

        @Synchronized
        fun getInstance(context: Context): LocalDatabase?{
            if(instance == null){
                synchronized(LocalDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LocalDatabase::class.java,
                        "Local-database"
                    )
                        .addMigrations().build()
                }
            }
            return instance
        }
    }

}

