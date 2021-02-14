package com.gehad.memorymap.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gehad.memorymap.dataBase.does.NotesDao
import com.gehad.memorymap.dataBase.model.Note

@Database(entities = arrayOf(Note::class) ,exportSchema = false ,version = 1)
@TypeConverters(Converters::class)
abstract class MyDataBase :RoomDatabase() {
    abstract fun notesDao(): NotesDao

    companion object{

       private var myDataBaseInstance: MyDataBase?=null

        fun getInstance(context:Context): MyDataBase?{

            if(myDataBaseInstance ==null){
                myDataBaseInstance = Room.databaseBuilder(context,
                    MyDataBase::class.java,"NotesDataBase")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()

            }

            return myDataBaseInstance
        }

    }
}