package com.himanshurawat.goldenhour.ui.saved

import android.content.Context
import com.himanshurawat.goldenhour.db.ItemDatabase
import com.himanshurawat.goldenhour.db.entity.Item
import org.jetbrains.anko.doAsync

class SavedActivityModelImpl(context: Context): SavedActivityContract.Model {

    private val database: ItemDatabase = ItemDatabase.getInstance(context)

    override fun delete(item: Item){
        doAsync {
            database.getDao().delete(item)
        }
    }

    override fun allItem(): List<Item> {
        return database.getDao().allItem()
    }



}