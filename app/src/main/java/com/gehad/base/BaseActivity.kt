package com.gehad.base

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

open class BaseActivity :AppCompatActivity() {

    lateinit var activity:AppCompatActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity=this
    }

    fun showMassage(title:String?,massage:String,
                    posActionName:String?,
                    posAction:DialogInterface.OnClickListener?,
                    negActionName:String?,
                    negAction:DialogInterface.OnClickListener?,
                    isCancelable:Boolean){

        val dailog=AlertDialog.Builder(this)
        dailog.setTitle(title)
        dailog.setMessage(massage)
        dailog.setPositiveButton(posActionName,posAction)
        dailog.setNegativeButton(negActionName,negAction)
        dailog.setCancelable(isCancelable)
        dailog.show()
    }

    fun showMassage(title:Int?,massage:Int,
                    posActionName:Int?,
                    posAction:DialogInterface.OnClickListener?,
                    negActionName:Int?,
                    negAction:DialogInterface.OnClickListener?,
                    isCancelable:Boolean){

        val dailog=AlertDialog.Builder(this)
        if(title!=null)
            dailog.setTitle(title)
        dailog.setMessage(massage)
        if(posActionName!=null)
            dailog.setPositiveButton(posActionName,posAction)
        if(negActionName!=null)
            dailog.setNegativeButton(negActionName,negAction)
        dailog.setCancelable(isCancelable)
        dailog.show()
    }

    fun isPermissionGranted(permission:String):Boolean{

        // if permission is granted return true..
        return (ContextCompat.checkSelfPermission(this,permission)
                == PackageManager.PERMISSION_GRANTED)
    }

}