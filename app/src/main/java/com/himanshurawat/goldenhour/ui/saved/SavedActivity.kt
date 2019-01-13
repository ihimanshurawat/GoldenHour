package com.himanshurawat.goldenhour.ui.saved

import android.os.Bundle
import android.view.View
import android.widget.Adapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.himanshurawat.goldenhour.R
import com.himanshurawat.goldenhour.adapter.LocationItemAdapter
import com.himanshurawat.goldenhour.db.entity.Item
import kotlinx.android.synthetic.main.activity_saved.*
import org.jetbrains.anko.find


class SavedActivity: AppCompatActivity(),SavedActivityContract.View,
    LocationItemAdapter.LocationItemClickListener {

    override fun populateData(data: List<Item>) {
        if(data.isEmpty()){
            activity_saved_text_view.visibility = View.VISIBLE
        }
        dataList.clear()
        dataList.addAll(data)
        adapter.notifyDataSetChanged()

    }


    override fun itemDeleted(pos: Int) {
        dataList.removeAt(pos)
        adapter.notifyItemRemoved(pos)
        if(dataList.isEmpty()){
            activity_saved_text_view.visibility = View.VISIBLE
        }
    }

    override fun locationItemClicked(item: Item,pos: Int) {

        //Finish to Previous Activity

    }

    override fun deleteButtonClicked(item: Item,pos: Int) {
        presenter.delete(item,pos)

    }


    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LocationItemAdapter
    private lateinit var dataList: MutableList<Item>
    private lateinit var presenter: SavedActivityContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)
        val ab = supportActionBar
        if(ab != null){
            ab.title = "Saved"
            ab.setDisplayHomeAsUpEnabled(true)
        }
        setup()
    }

    private fun setup(){
        presenter = SavedActivityPresenterImpl(this)
        presenter.initModel(this)
        recyclerView = find(R.id.activity_saved_recycler_view)
        dataList = arrayListOf()
        adapter = LocationItemAdapter(dataList,this,this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        presenter.populate()
    }
}