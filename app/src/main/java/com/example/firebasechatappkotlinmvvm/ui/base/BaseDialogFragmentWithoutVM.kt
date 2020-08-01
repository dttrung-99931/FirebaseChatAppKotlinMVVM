package com.example.firebasechatappkotlinmvvm.ui.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.fragment.app.DialogFragment
import com.example.firebasechatappkotlinmvvm.R
import java.lang.Exception


/**
 * Created by Trung on 7/19/2020
 */
abstract class BaseDialogFragmentWithoutVM: DialogFragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
    }

    protected fun finishActivity() {
        mBaseActivity.let {
            mBaseActivity.finish()
        }
    }

    abstract fun getLayoutResId(): Int
    abstract fun setupViews()
}