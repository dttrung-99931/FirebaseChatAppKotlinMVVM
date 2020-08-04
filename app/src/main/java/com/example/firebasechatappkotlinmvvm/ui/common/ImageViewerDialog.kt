package com.example.firebasechatappkotlinmvvm.ui.common

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.ui.base.BaseDialogFragmentWithoutVM
import com.github.piasy.biv.view.ImageShownCallback
import kotlinx.android.synthetic.main.dialog_image_viewer.*


/**
 * Created by Trung on 8/4/2020
 */
class ImageViewerDialog(private val imgUrl: String):
    BaseDialogFragmentWithoutVM() {

    companion object{
        fun show(imgUrl: String, fragmentManager: FragmentManager){
            ImageViewerDialog(imgUrl).show(fragmentManager, "")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make the dialog fullscreen with status bar
        setStyle(STYLE_NORMAL ,
            android.R.style.Theme_Black_NoTitleBar)
    }
    override fun getLayoutResId(): Int {
        return R.layout.dialog_image_viewer
    }

    override fun setupViews() {
        mImageViewer.setImageShownCallback(object : ImageShownCallback {
            override fun onThumbnailShown() {
            }

            override fun onMainImageShown() {
                mProgressBar.visibility = View.GONE
            }
        })
        val imgUri = Uri.parse(imgUrl)
        mImageViewer.showImage(imgUri)
    }
}