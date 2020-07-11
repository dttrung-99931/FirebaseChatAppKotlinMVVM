package com.example.firebasechatappkotlinmvvm.di.component

import android.app.Application
import com.example.firebasechatappkotlinmvvm.di.App
import com.example.firebasechatappkotlinmvvm.di.builder.ActivityBuilder
import com.example.firebasechatappkotlinmvvm.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


/**
 * Created by Trung on 7/10/2020
 */
@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, ActivityBuilder::class])
interface AppComponent {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}