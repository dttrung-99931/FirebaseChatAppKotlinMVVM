package com.example.firebasechatappkotlinmvvm.ui.base

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.data.callback.SingleCallBack
import com.google.firebase.FirebaseNetworkException
import com.roger.catloadinglibrary.CatLoadingView

open class BaseActivity : AppCompatActivity() {
    companion object{
        const val REQUEST_CODE_PICK_IMG = 1234
        const val REQUEST_CODE_CAPTURE_IMG = 4321
    }

    lateinit var loadingView: CatLoadingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupGlobalExceptionHandler()
        setupViews()
    }

    fun isNetworkAvailable(): Boolean {
        val service = getSystemService(Context.CONNECTIVITY_SERVICE)
        if (service is ConnectivityManager)
            return service.activeNetwork != null
        return false
    }

    private fun setupGlobalExceptionHandler() {
        val oldHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { t: Thread, e: Throwable ->
            if (e is FirebaseNetworkException)
                showToastMsg("No internet")
            else
            oldHandler?.uncaughtException(t, e)
        }
    }

    private fun setupViews() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        loadingView = CatLoadingView()
        loadingView.setBackgroundColor(Color.TRANSPARENT)
        loadingView.isCancelable = false
        loadingView.setClickCancelAble(false)
    }

    fun showLoading() {
        if (!loadingView.isVisible)
            loadingView.show(supportFragmentManager, "")
    }

    fun hideLoading() {
        loadingView.let { loadingView.dismiss() }
    }

    fun showToastMsg(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT)?.show()
    }

    fun showToastMsg(msgResId: Int){
        Toast.makeText(this, msgResId, Toast.LENGTH_SHORT)?.show()
    }

    fun showConfirmDialog(msgResId: Int,
                          onYes: DialogInterface.OnClickListener,
                          onNo: DialogInterface.OnClickListener? = null) {
        val alertDialog = AlertDialog.Builder(this).create()

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
            getString(R.string.yes), onYes)

        if (onNo != null)
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                getString(R.string.no), onNo)
        else alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
            getString(R.string.no)
        ) { _, _ -> }

        val msg = getString(msgResId)
        alertDialog.setMessage(msg)

        alertDialog.show()
    }

    // Called in OnActivityResult
    private var mSelectMediaImgCallBack: SingleCallBack<Uri>? = null

    fun selectMediaImage(selectImgCallBack: SingleCallBack<Uri>) {
        mSelectMediaImgCallBack = selectImgCallBack
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_PICK_IMG)
    }

    // Called in OnActivityResult
    private var mCaptureImgCallBack: SingleCallBack<Bitmap>? = null

    fun captureImage(captureImgCallBack: SingleCallBack<Bitmap>) {
        mCaptureImgCallBack = captureImgCallBack
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CODE_CAPTURE_IMG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_PICK_IMG -> {
                if (resultCode == Activity.RESULT_OK){
                    data?.data?.let { mSelectMediaImgCallBack?.onSuccess(it) }
                    mSelectMediaImgCallBack = null
                }
            }
            REQUEST_CODE_CAPTURE_IMG -> {
                if (resultCode == Activity.RESULT_OK && mCaptureImgCallBack != null){
                    val bitmap = data?.extras?.get("data") as Bitmap
                    mCaptureImgCallBack!!.onSuccess(bitmap)
                    mCaptureImgCallBack = null
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
