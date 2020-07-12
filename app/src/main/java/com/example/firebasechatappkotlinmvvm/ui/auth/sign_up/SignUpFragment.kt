package com.example.firebasechatappkotlinmvvm.ui.auth.sign_up

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.firebasechatappkotlinmvvm.BR
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.databinding.FragmentLoginBinding
import com.example.firebasechatappkotlinmvvm.ui.base.BaseFragment
import javax.inject.Inject

class SignUpFragment : BaseFragment<FragmentLoginBinding, SignUpViewModel>() {
    @Inject
    lateinit var mFactory: SignUpViewModel.Factory

    override fun getLayoutResId(): Int {
        return R.layout.fragment_sign_up
    }

    override fun getVMBindingVarId(): Int {
        return BR.viewModel;
    }

    override fun getVM(): SignUpViewModel {
        return ViewModelProviders
            .of(this, mFactory)[SignUpViewModel::class.java]
    }

    override fun setupViews() {
        mVm.mLoggedInUser.observe(this, Observer {
            showToastMsg(it.username)
        })
    }

}