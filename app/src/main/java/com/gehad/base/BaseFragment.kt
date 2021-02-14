package com.gehad.base

import android.content.Context
import android.content.DialogInterface
import androidx.fragment.app.Fragment

open class BaseFragment :Fragment() {

    lateinit var activity: BaseActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity=context as BaseActivity
    }

    fun showMassage(title:String?,massage:String,
                    posActionName:String?,
                    posAction:DialogInterface.OnClickListener?,
                    negActionName:String?,
                    negAction:DialogInterface.OnClickListener?,
                    isCancelable:Boolean){

      activity.showMassage(title, massage, posActionName,
          posAction, negActionName, negAction, isCancelable)
    }

    fun showMassage(title:Int?,massage:Int,
                    posActionName:Int?,
                    posAction:DialogInterface.OnClickListener?,
                    negActionName:Int?,
                    negAction:DialogInterface.OnClickListener?,
                    isCancelable:Boolean){

        activity.showMassage(title, massage, posActionName,
            posAction, negActionName, negAction, isCancelable)
    }

}