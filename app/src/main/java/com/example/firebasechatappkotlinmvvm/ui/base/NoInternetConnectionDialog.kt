package com.example.firebasechatappkotlinmvvm.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.firebasechatappkotlinmvvm.R


/**
 * Created by Trung on 7/19/2020
 */
class NoInternetConnectionDialog: BaseDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_no_internet, container, false)
    }

    override fun onDestroyView() {
        finishActivity()
        super.onDestroyView()
    }
}