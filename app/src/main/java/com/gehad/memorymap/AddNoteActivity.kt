package com.gehad.memorymap

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import com.gehad.memorymap.dataBase.MyDataBase
import com.gehad.memorymap.dataBase.model.Note
import com.gehad.memorymap.pojo.Constant
import com.gehad.base.BaseActivity
import kotlinx.android.synthetic.main.activity_add_note.*
import java.text.SimpleDateFormat
import java.util.*

class AddNoteActivity : BaseActivity(),View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        add_btn.setOnClickListener(this)
        image_btn.setOnClickListener(this)

        // Request for camera permission
        if (!isPermissionGranted(android.Manifest.permission.CAMERA)){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.CAMERA),
                Constant.ACCESS_CAMERA_REQUEST_CODE)
        }
    }

    override fun onClick(view: View?) {
        if(view?.id==R.id.add_btn){
            addNote()
        }
        if(view?.id==R.id.image_btn){
            takePhoto()
        }
    }

    private fun takePhoto(){
        //open camera
        val intent =Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent,Constant.ACCESS_CAMERA_REQUEST_CODE)
    }

    private var captureImage:Bitmap?=null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==Constant.ACCESS_CAMERA_REQUEST_CODE){
            // get capture image
            captureImage = data?.extras?.get("data") as Bitmap
            //set capture image to image view
            image_note.setImageBitmap(captureImage)
        }
    }
    private fun addNote() {
        val latitude=intent.getDoubleExtra(Constant.LATITUDE,-1.0)
        val longitude=intent.getDoubleExtra(Constant.LONGITUDE,-1.0)
        val titleNote =title_note.text.toString()
        val description= description_note.text.toString()
        // to get date and time
        val time = SimpleDateFormat("HH:mm:ss  dd/MM/yyyy", Locale.getDefault())

        val date=time.format(Date()).toString()

        val image: Bitmap?
        if (captureImage !=null)image=captureImage
        else image=null

        if(description!=""||titleNote!="") {
            val note = Note(
                title = titleNote,
                description = description,
                time = date,
                latitude = latitude,
                longitude = longitude,
                image = image
            )

            MyDataBase.getInstance(applicationContext)?.notesDao()?.insertNote(note)
            showSuccessMessage()
        }
    }

    private fun showSuccessMessage(){
        showMassage(null,R.string.note_created_successfully,
            R.string.ok,DialogInterface.OnClickListener{dialogInterface, _ ->
            dialogInterface.dismiss()
            finish()
             },null,null,true)
    }

}
