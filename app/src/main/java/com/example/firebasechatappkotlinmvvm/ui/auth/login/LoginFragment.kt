package com.example.firebasechatappkotlinmvvm.ui.auth.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.firebasechatappkotlinmvvm.BR
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.databinding.FragmentLoginBinding
import com.example.firebasechatappkotlinmvvm.ui.base.BaseFragment
import com.example.firebasechatappkotlinmvvm.ui.main.MainActivity
import com.example.firebasechatappkotlinmvvm.util.AppConstants
import javax.inject.Inject

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    @Inject
    lateinit var mFactory: LoginViewModel.Factory

    override fun getLayoutResId(): Int {
        return R.layout.fragment_login
    }

    override fun getVMBindingVarId(): Int {
        return BR.viewModel;
    }

    override fun getVM(): LoginViewModel {
        return ViewModelProviders
            .of(this, mFactory)[LoginViewModel::class.java]
    }

    override fun setupViews() {
    }

    override fun observe() {
        vm.onLoginFailure.observe(this, Observer {
            when (it){
                AppConstants.AuthErr.LOGIN_FAILED -> showToastMsg(R.string.login_failed)
                AppConstants.CommonErr.UNKNOWN -> showToastMsg(R.string.sth_went_wrong)
            }
        })

        vm.onLoginSuccess.observe(this, Observer {
            MainActivity.open(requireContext())
            finishActivity()
        })
    }

}