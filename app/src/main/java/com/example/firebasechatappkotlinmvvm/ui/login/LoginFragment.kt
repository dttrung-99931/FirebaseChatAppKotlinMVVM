package com.example.firebasechatappkotlinmvvm.ui.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.firebasechatappkotlinmvvm.BR
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.databinding.FragmentLoginBinding
import com.example.firebasechatappkotlinmvvm.ui.base.BaseFragment
import javax.inject.Inject

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    @Inject
    lateinit var mFactory: LoginViewModel.Factory

    override fun getLayoutResId(): Int {
        return R.layout.fragment_login
    }

    override fun getVMBindingVarId(): Int {
        return BR.viewModel
    }

    override fun getVM(): LoginViewModel {
        return ViewModelProviders
            .of(this, mFactory)[LoginViewModel::class.java];
    }

    override fun setupViews() {
        mVm.mLoggedInUser.observe(this, Observer {
            showToastMsg(it.username)
        })
    }

}