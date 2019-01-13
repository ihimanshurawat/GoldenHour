package com.himanshurawat.goldenhour.ui.saved

import android.content.Context
import com.himanshurawat.goldenhour.db.entity.Item

class SavedActivityPresenterImpl(private val view: SavedActivityContract.View): SavedActivityContract.Presenter {

    override fun populate() {
        view.populateData(model.allItem())
    }

    private lateinit var model: SavedActivityContract.Model


    override fun delete(item: Item,pos: Int) {
        model.delete(item)
        view.itemDeleted(pos)
    }


    override fun initModel(context: Context) {
        model = SavedActivityModelImpl(context)
    }

}