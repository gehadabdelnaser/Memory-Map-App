package com.gehad.memorymap


import android.os.Bundle
import com.gehad.base.BaseActivity
import com.gehad.memorymap.dataBase.MyDataBase
import com.gehad.memorymap.dataBase.model.Note
import com.gehad.memorymap.pojo.Constant
import kotlinx.android.synthetic.main.activity_note_details.*
import kotlinx.android.synthetic.main.activity_note_details.description_note
import kotlinx.android.synthetic.main.activity_note_details.image_note
import kotlinx.android.synthetic.main.activity_note_details.title_note

class NoteDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)


        showNoteDetails()
    }

    private fun getNote(): Note? {
        val latitude=intent.getDoubleExtra(Constant.LATITUDE,-1.0)
        val longitude=intent.getDoubleExtra(Constant.LONGITUDE,-1.0)

        return MyDataBase.getInstance(applicationContext)
            ?.notesDao()
            ?.searchNotesByLatitudeAndLongitude(latitude,longitude)
    }

    private fun showNoteDetails(){

        val note=getNote()
        date_tv.text = note?.time.toString()
        title_note.text = note?.title.toString()
        description_note.text = note?.description.toString()
        image_note.setImageBitmap(note?.image)
    }

}
