package com.example.firebasechatappkotlinmvvm.ui.base

import android.os.Handler
import androidx.lifecycle.Observer
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.ui.start.StartViewModel
import kotlinx.android.synthetic.main.dialog_no_internet.*


/**
 * Created by Trung on 7/19/2020
 */
class NoInternetConnectionDialog(val startVM: StartViewModel) : BaseDialogFragmentWithoutVM() {
    override fun getLayoutResId(): Int {
        return R.layout.dialog_no_internet
    }

    override fun setupViews() {
        mRefreshLayout.setOnRefreshListener {
            if (mBaseActivity.isNetworkAvailable())
                Handler().post {
                    startVM.processNavToAuthOrMainActivity()
                }
            else mRefreshLayout.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        finishActivity()
        super.onDestroyView()
    }
}