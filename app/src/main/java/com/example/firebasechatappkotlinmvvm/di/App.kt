package com.example.firebasechatappkotlinmvvm.di

import android.app.Activity
import android.app.Application
import android.app.Service
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
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.glide.GlideImageLoader
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.ios.IosEmojiProvider
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class App: Application(), HasActivityInjector, HasServiceInjector,
    LifecycleObserver, Application.ActivityLifecycleCallbacks{

    @Inject
    lateinit var mActivityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var mServiceInjector: DispatchingAndroidInjector<Service>

    @Inject
    lateinit var mUserRepo: UserRepo

    var activityCount = 0
        private set

    /*
    * App in background means there is no activity running
    * but there can be app's services running
    * */
    fun isAppInBackground(): Boolean{
        return activityCount == 0
    }

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

        BigImageViewer.initialize(GlideImageLoader.with(this))

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        registerActivityLifecycleCallbacks(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onApplicationStart(){
        mUserRepo.updateUserOnline()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onApplicationStop(){
        mUserRepo.updateUserOffline()
    }

    override fun serviceInjector(): AndroidInjector<Service> {
        return mServiceInjector
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activityCount++
    }

    override fun onActivityDestroyed(activity: Activity) {
        activityCount--
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }
}
