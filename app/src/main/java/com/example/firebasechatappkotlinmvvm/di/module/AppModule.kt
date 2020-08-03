package com.example.firebasechatappkotlinmvvm.di.module

import com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth.FireBaseAuthService
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth.FireBaseAuthServiceImpl
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_storage.FireBaseStorageService
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_storage.FireBaseStorageServiceImpl
import com.example.firebasechatappkotlinmvvm.data.remote.firestore.FireStoreService
import com.example.firebasechatappkotlinmvvm.data.remote.firestore.FireStoreServiceImpl
import com.example.firebasechatappkotlinmvvm.data.remote.send_notification.NotificationSenderService
import com.example.firebasechatappkotlinmvvm.data.repo.chat.ChatRepo
import com.example.firebasechatappkotlinmvvm.data.repo.chat.ChatRepoImpl
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepoImpl
import com.example.firebasechatappkotlinmvvm.util.AppConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * Created by Trung on 7/10/2020
 */
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideFirestore() = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideFireBaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFireStoreService(fireStoreServiceImpl: FireStoreServiceImpl):
            FireStoreService = fireStoreServiceImpl

    @Singleton
    @Provides
    fun provideFireBaseAuthService(fireBaseAuthServiceImpl: FireBaseAuthServiceImpl):
            FireBaseAuthService = fireBaseAuthServiceImpl

    @Singleton
    @Provides
    fun provideUserRepo(userRepoImpl: UserRepoImpl): UserRepo = userRepoImpl

    @Singleton
    @Provides
    fun provideFireBaseStorage() = FirebaseStorage.getInstance()

    @Singleton
    @Provides
    fun provideFireBaseStorageService(storageImpl: FireBaseStorageServiceImpl) :
            FireBaseStorageService = storageImpl

    @Singleton
    @Provides
    fun provideChatRepo(chatRepoImpl: ChatRepoImpl): ChatRepo = chatRepoImpl

    @Singleton
    @Provides
    fun provideNotificationClient(): Retrofit = Retrofit.Builder()
        .baseUrl("https://fcm.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideNotificationSenderService(notificationClient: Retrofit):
        NotificationSenderService = notificationClient
            .create(NotificationSenderService::class.java)
}