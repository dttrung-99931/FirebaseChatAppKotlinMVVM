package com.example.firebasechatappkotlinmvvm.di.builder

import com.example.firebasechatappkotlinmvvm.service.NotificationReceiverService
import com.example.firebasechatappkotlinmvvm.service.UpdateTokenService
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Trung on 7/10/2020
 */
@Module
abstract class ServiceBuilder {
    @ContributesAndroidInjector
    abstract fun bindUpdateTokenService(): UpdateTokenService

    @ContributesAndroidInjector
    abstract fun bindNotificationService(): NotificationReceiverService
}