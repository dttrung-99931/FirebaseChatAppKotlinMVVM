package com.example.firebasechatappkotlinmvvm.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment

abstract class BaseFragment<TViewBinding: ViewDataBinding, TVModel: ViewModel> : Fragment() {
    lateinit var mVm: TVModel
    lateinit var mViewBinding: TViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        mVm = getVM()
    }

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mViewBinding = DataBindingUtil.inflate(
            inflater, getLayoutResId(), container, false)
        return mViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewBinding()
        setupViews();
    }

    fun setupViewBinding(){
        mViewBinding.lifecycleOwner = this
        mViewBinding.setVariable(getVMBindingVarId(), mVm)
        mViewBinding.executePendingBindings()
    }

    fun showToastMsg(msg: String){
        Toast.makeText(this.context, msg, Toast.LENGTH_SHORT)?.show()
    }

    abstract fun getLayoutResId(): Int
    abstract fun getVMBindingVarId(): Int
    abstract fun getVM(): TVModel
    abstract fun setupViews()
}
