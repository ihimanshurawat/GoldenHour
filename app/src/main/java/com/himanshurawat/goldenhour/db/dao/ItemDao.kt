package com.himanshurawat.goldenhour.db.dao

import androidx.room.*
import com.himanshurawat.goldenhour.db.entity.Item

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Item)

    @Query("SELECT * FROM item")
    fun allItem(): List<Item>

    @Delete
    fun delete(item: Item)
}