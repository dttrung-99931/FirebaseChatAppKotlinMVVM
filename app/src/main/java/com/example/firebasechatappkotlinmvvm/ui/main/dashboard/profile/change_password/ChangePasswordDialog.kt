package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.profile.change_password

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.firebasechatappkotlinmvvm.BR
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.databinding.DialogChangePasswordBinding
import com.example.firebasechatappkotlinmvvm.ui.base.BaseDialogFragment
import com.example.firebasechatappkotlinmvvm.util.AppConstants
import com.example.firebasechatappkotlinmvvm.util.extension.showKeyBoard
import kotlinx.android.synthetic.main.dialog_change_password.*
import javax.inject.Inject

class ChangePasswordDialog : BaseDialogFragment<
        DialogChangePasswordBinding, ChangePasswordViewModel>() {

    @Inject
    lateinit var mFactory: ChangePasswordViewModel.Factory

    override fun getLayoutResId(): Int {
        return R.layout.dialog_change_password
    }

    override fun getVMBindingVarId(): Int {
        return BR.viewModel;
    }

    override fun getVM(): ChangePasswordViewModel {
        return ViewModelProviders
            .of(this, mFactory)[ChangePasswordViewModel::class.java]
    }

    override fun setupViews() {
        mEdtOldPassword.showKeyBoard()
    }

    override fun observe() {
        vm.changePasswordResult.observe(this, Observer {
            when (it) {
                AppConstants.OK -> {
                    showToastMsg(R.string.successfully)
                    dismiss()
                }
                AppConstants.AuthErr.MISSING_INFORMATION
                        -> showToastMsg(R.string.please_fill_correct_info)

                AppConstants.AuthErr.WEAK_PASSWORD
                        -> showToastMsg(R.string.weak_password)

                AppConstants.AuthErr.INCORRECT_OLD_PASSWORD
                        -> showToastMsg(R.string.incorrect_old_password)
            }
        })
    }

}