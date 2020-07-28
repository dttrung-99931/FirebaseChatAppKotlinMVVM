package com.example.firebasechatappkotlinmvvm.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth.FireBaseAuthService
import com.example.firebasechatappkotlinmvvm.data.remote.firestore.FireStoreService
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import com.example.firebasechatappkotlinmvvm.di.component.DaggerAppComponent
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.ios.IosEmojiProvider
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class App: Application(), HasActivityInjector, LifecycleObserver{
    @Inject
    lateinit var mActivityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var mUserRepo: UserRepo

    override fun activityInjector(): AndroidInjector<Activity> {
        return mActivityInjector
    }

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)

        EmojiManager.install(IosEmojiProvider())

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onApplicationStart(){
        mUserRepo.updateUserOnline()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onApplicationStop(){
        mUserRepo.updateUserOffline()
    }
}
