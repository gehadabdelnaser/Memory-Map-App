package com.gehad.memorymap.dataBase.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Note(
    @ColumnInfo
    var title:String,
    @ColumnInfo
    var description:String,
    @ColumnInfo
    var time:String,
    @ColumnInfo
    var latitude:Double,
    @ColumnInfo
    var longitude:Double,
    @ColumnInfo
    var image: Bitmap?
)
{
    @PrimaryKey(autoGenerate = true)
     var id:Int?=null
}