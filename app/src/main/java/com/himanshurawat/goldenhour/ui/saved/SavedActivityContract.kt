package com.himanshurawat.goldenhour.ui.saved

import android.content.Context
import com.himanshurawat.goldenhour.db.entity.Item

interface SavedActivityContract {

    interface Model{
        fun delete(item: Item)
        fun allItem(): List<Item>
    }

    interface View {
        fun itemDeleted(pos: Int)
        fun populateData(data: List<Item>)
    }

    interface Presenter{
        fun populate()
        fun initModel(context: Context)
        fun delete(item: Item,pos: Int)


    }


}