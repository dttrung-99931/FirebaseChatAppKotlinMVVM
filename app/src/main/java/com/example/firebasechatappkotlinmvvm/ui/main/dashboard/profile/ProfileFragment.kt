package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.profile

import android.content.DialogInterface
import android.graphics.Bitmap
import android.net.Uri
import android.view.Menu
import android.view.MenuInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.firebasechatappkotlinmvvm.BR
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.data.callback.SingleCallBack
import com.example.firebasechatappkotlinmvvm.databinding.FragmentChatListBinding
import com.example.firebasechatappkotlinmvvm.ui.auth.AuthActivity
import com.example.firebasechatappkotlinmvvm.ui.base.BaseFragment
import com.example.firebasechatappkotlinmvvm.ui.base.OnItemWithPositionClickListener
import com.example.firebasechatappkotlinmvvm.ui.base.OptionBottomSheetDialogFragment
import com.example.firebasechatappkotlinmvvm.ui.common.ImageViewerDialog
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.profile.change_password.ChangePasswordDialog
import com.example.firebasechatappkotlinmvvm.util.AppConstants
import com.example.firebasechatappkotlinmvvm.util.extension.toInputStream
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class ProfileFragment : BaseFragment<FragmentChatListBinding, ProfileViewModel>() {

    @Inject
    lateinit var mVMFactory: ProfileViewModel.Factory

    override fun getLayoutResId(): Int {
        return R.layout.fragment_profile
    }

    override fun getVMBindingVarId(): Int {
        return BR.viewModel;
    }

    override fun getVM(): ProfileViewModel {
        return ViewModelProvider(this, mVMFactory)
            .get(ProfileViewModel::class.java)
    }

    override fun setupViews() {
        setupAvatarOption()
        setupProfileOptionRecyclerView()
        vm.loadUserProfile()
    }

    private fun setupAvatarOption() {
        mImgAvatar.setOnClickListener {
            OptionBottomSheetDialogFragment(
                AppConstants.STR_IDS_AVATAR_OPTION,
                object : OnItemWithPositionClickListener {
                    override fun onItemWithPositionClicked(position: Int) {
                        when (position) {
                            0 -> uploadAvatarWithCapturedImage()
                            1 -> uploadAvatarWithSelectedImage()
                            2 -> vm.onSeeAvatarOptionClicked()
                        }
                    }
                }
            ).show(childFragmentManager, "")
        }
    }

    private val mCaptureImgCallBack: SingleCallBack<Bitmap>
        = object : SingleCallBack<Bitmap> {
        override fun onSuccess(avatarBitmap: Bitmap) {
            vm.uploadAvatar(avatarBitmap.toInputStream())
            mImgAvatar.setImageBitmap(avatarBitmap)
        }
    }

    private fun uploadAvatarWithCapturedImage() {
        captureImage(mCaptureImgCallBack)
    }

    private val mSelectMediaImgUriCallBack : SingleCallBack<Uri>
        = object : SingleCallBack<Uri> {
        override fun onSuccess(avatarUri: Uri) {
            vm.uploadAvatar(openInputStream(avatarUri))
            mImgAvatar.setImageURI(avatarUri)
        }
    }

    private fun uploadAvatarWithSelectedImage() {
        selectMediaImage(mSelectMediaImgUriCallBack)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_avatar_profile, menu)
    }

    private fun showExitConfirmDialog() {
        showConfirmDialog(R.string.are_you_sure,
            DialogInterface.OnClickListener { dialog, which ->
                vm.signOut()
                AuthActivity.open(requireContext())
                finishActivity()
            })
    }

    private val mOnItemProfileOptionClick: OnItemWithPositionClickListener =
        object : OnItemWithPositionClickListener {
            override fun onItemWithPositionClicked(position: Int) {
                when (position) {
                    0 -> ChangePasswordDialog().show(childFragmentManager, "")
                    1 -> showExitConfirmDialog ()
                }
            }
        }

    private fun setupProfileOptionRecyclerView() {
        mRecyclerView.adapter = ProfileOptionAdapter(mOnItemProfileOptionClick)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.setHasFixedSize(true)
    }

    override fun observe() {
        vm.curAppUser.observe(this, Observer {
            mTvNickname.text = it.nickname
            if (it.avatarUrl.isNotEmpty())
                Glide.with(requireContext())
                    .load(it.avatarUrl)
                    .placeholder(R.drawable.ic_no_avatar_100px)
                    .centerCrop()
                    .into(mImgAvatar)
        })

        vm.onOpenAvatarImgViewer.observe(this, Observer {
            if (it.isNotEmpty()) {
                ImageViewerDialog.show(it, childFragmentManager)
            } else showToastMsg(R.string.no_avatar)
        })

    }

}