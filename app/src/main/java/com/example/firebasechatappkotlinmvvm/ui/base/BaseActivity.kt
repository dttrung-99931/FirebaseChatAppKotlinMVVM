package com.example.firebasechatappkotlinmvvm.ui.base

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.roger.catloadinglibrary.CatLoadingView
import dagger.android.AndroidInjection
import dagger.android.DaggerActivity

open class BaseActivity : AppCompatActivity() {
    lateinit var loadingView: CatLoadingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        loadingView = CatLoadingView()
        loadingView.setBackgroundColor(Color.parseColor("#000000"))
    }

    fun showLoading() {
        if (!loadingView.isVisible)
            loadingView.show(supportFragmentManager, "")
    }

    fun hideLoading() {
        loadingView.let { loadingView.dismiss() }
    }
}
