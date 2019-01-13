package com.himanshurawat.goldenhour.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.himanshurawat.goldenhour.R
import com.himanshurawat.goldenhour.db.entity.Item
import org.jetbrains.anko.find

class LocationItemAdapter(private val dataList: MutableList<Item>, private val context: Context, private val listener: LocationItemClickListener): RecyclerView.Adapter<LocationItemAdapter.LocationItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationItemViewHolder {
        return LocationItemViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_item,parent,false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: LocationItemViewHolder, position: Int) {
        val pos = holder.adapterPosition
        val item = dataList[pos]
        holder.latTextView.text = "Lat - ${item.lat}"
        holder.longTextView.text = "Long - ${item.lng}"
        holder.deleteImageView.setOnClickListener{
            listener.deleteButtonClicked()
        }
        holder.itemView.setOnClickListener{
            listener.locationItemClicked()
        }

    }


    class LocationItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val latTextView = itemView.find<TextView>(R.id.recycler_view_item_latitude)
        val longTextView = itemView.find<TextView>(R.id.recycler_view_item_longitude)
        val deleteImageView = itemView.find<ImageView>(R.id.recycler_view_item_delete)
    }

    interface LocationItemClickListener{
        fun locationItemClicked()
        fun deleteButtonClicked()
    }
}