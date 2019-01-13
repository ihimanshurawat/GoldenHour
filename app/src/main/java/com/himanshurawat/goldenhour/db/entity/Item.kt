package com.himanshurawat.goldenhour.db.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "item")
class Item(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id:Long = 0,
    @ColumnInfo(name = "lat")
    var lat:Double,
    @ColumnInfo(name = "long")
    var lng: Double,
    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0
): Serializable