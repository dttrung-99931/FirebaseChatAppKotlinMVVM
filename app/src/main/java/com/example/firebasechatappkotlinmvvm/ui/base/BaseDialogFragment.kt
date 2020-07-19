package com.example.firebasechatappkotlinmvvm.ui.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.firebasechatappkotlinmvvm.R
import java.lang.Exception


/**
 * Created by Trung on 7/19/2020
 */
open class BaseDialogFragment: DialogFragment() {
    lateinit var mBaseActivity: BaseActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) mBaseActivity = context
        else throw Exception("All Activities must be children of BaseActivity")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            R.style.FullScreenDialogFragment)
    }

    protected fun finishActivity() {
        mBaseActivity.let {
            mBaseActivity.finish()
        }
    }

}