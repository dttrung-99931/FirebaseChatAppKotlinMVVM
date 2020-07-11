package com.example.firebasechatappkotlinmvvm.di

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.example.firebasechatappkotlinmvvm.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class App: Application(), HasActivityInjector{
    @Inject
    lateinit var mActivityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> {
        return mActivityInjector
    }

    override fun onCreate() {
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
        super.onCreate()
    }

}
