package com.example.firebasechatappkotlinmvvm.ui.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseNetworkException
import com.roger.catloadinglibrary.CatLoadingView
import dagger.android.AndroidInjection
import dagger.android.DaggerActivity

open class BaseActivity : AppCompatActivity() {
    lateinit var loadingView: CatLoadingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupGlobalExceptionHandler()
        setupViews()
    }

    public fun checkNetwork() {
        if (!isNetworkAvailable())
            openNoInternetFragment()
    }

    public fun openNoInternetFragment() {
        NoInternetConnectionDialog().show(supportFragmentManager, "")
    }

    public fun isNetworkAvailable(): Boolean {
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
        loadingView.setBackgroundColor(Color.parseColor("#000000"))
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

}
