package com.gehad.memorymap.dataBase.does

import androidx.room.*
import com.gehad.memorymap.dataBase.model.Note

@Dao
interface NotesDao {

    @Insert
    fun insertNote(note: Note)
    @Delete
    fun deleteNote(note: Note)

    @Query("delete from Note where id =:id")
    fun deleteNoteById(id:Int)

    @Update
    fun updateNote(note: Note)

    @Query("select * from Note")
    fun getAllNotes():List<Note>

    @Query("select * from Note where description like :word")
    fun searchNotesByDescription(word:String):List<Note>

    @Query("select * from Note where id like :id")
    fun searchNotesById(id:Int): Note

    @Query("select * from Note where latitude like :latitude and longitude like :longitude")
    fun searchNotesByLatitudeAndLongitude(latitude:Double ,longitude:Double): Note
}