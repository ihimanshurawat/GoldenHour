package com.himanshurawat.goldenhour.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.himanshurawat.goldenhour.db.dao.ItemDao
import com.himanshurawat.goldenhour.db.entity.Item

@Database(entities = [(Item::class)],version = 1, exportSchema = false)
abstract class ItemDatabase: RoomDatabase() {

    abstract fun getDao(): ItemDao

    companion object {
        private var INSTANCE:ItemDatabase? = null

        fun getInstance(context: Context): ItemDatabase{
            if(INSTANCE == null){
                synchronized(ItemDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        ItemDatabase::class.java, "database")
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE as ItemDatabase
        }
    }

}