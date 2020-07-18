package com.example.firebasechatappkotlinmvvm.ui.auth.sign_up

import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.firebasechatappkotlinmvvm.BR
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.databinding.FragmentSignUpBinding
import com.example.firebasechatappkotlinmvvm.ui.base.BaseFragment
import com.example.firebasechatappkotlinmvvm.util.AppConstants
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.mEdtNickname
import kotlinx.android.synthetic.main.fragment_sign_up.mEdtPassword
import javax.inject.Inject

class SignUpFragment : BaseFragment<FragmentSignUpBinding, SignUpViewModel>() {
    @Inject
    lateinit var mVmFactory: SignUpViewModel.Factory

    override fun getLayoutResId(): Int {
        return R.layout.fragment_sign_up
    }

    override fun getVMBindingVarId(): Int {
        return BR.viewModel;
    }

    override fun getVM(): SignUpViewModel {
        return ViewModelProviders
            .of(this, mVmFactory)[SignUpViewModel::class.java]
    }

    override fun setupViews() {
        mEdtEmail.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && mEdtEmail.error.isNullOrEmpty())
                vm.onTypeEmailComplete()
        }
        mEdtNickname.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && mEdtNickname.error.isNullOrEmpty())
                vm.onTypeNicknameComplete()
            else vm.isValidUsername = false
        }
        mEdtPassword.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && mEdtPassword.error.isNullOrEmpty())
                vm.onTypePasswordComplete()
        }

        mBtnSignUp.setOnClickListener {
            if (!mEdtNickname.text.isNullOrEmpty()
                && !mEdtEmail.text.isNullOrEmpty()
                && !mEdtPassword.text.isNullOrEmpty()
                && mEdtNickname.error.isNullOrEmpty()
                && mEdtEmail.error.isNullOrEmpty()
                && mEdtPassword.error.isNullOrEmpty())
                vm.onBtnSignUpClicked()
            else showToastMsg(R.string.please_fill_correct_info)
        }
    }

    override fun observe() {
        vm.onSignUpSuccess.observe(this, Observer {
            showToastMsg(getString(R.string.sign_up_successfully))
            mEdtNickname.setText("")
            mEdtEmail.setText("")
            mEdtPassword.setText("")
        })

        vm.onSignUpFailureWithCode.observe(this, Observer {
            when (it) {
                AppConstants.AuthErr.UNAVAILABLE_EMAIL ->
                    mEdtEmail.error = getString(R.string.unavailable_email)

                AppConstants.AuthErr.INVALID_EMAIL_FORMAT ->
                    mEdtEmail.error = getString(R.string.invalid_email_format)

                AppConstants.AuthErr.UNAVAILABLE_NICKNAME ->
                    mEdtNickname.error = getString(R.string.unavailable_nickname)

                AppConstants.AuthErr.WEAK_PASSWORD ->
                    mEdtPassword.error = getString(R.string.weak_password)

                AppConstants.CommonErr.UNKNOWN ->
                    showToastMsg(R.string.sth_went_wrong)
            }
        })
    }

}