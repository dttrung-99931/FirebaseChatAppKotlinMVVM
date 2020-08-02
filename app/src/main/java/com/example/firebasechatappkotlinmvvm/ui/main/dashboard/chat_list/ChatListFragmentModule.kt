package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.chat_list

import com.example.firebasechatappkotlinmvvm.data.repo.chat.ChatRepo
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import dagger.Module
import dagger.Provides
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
@Module
class ChatListFragmentModule {
    @Provides
    fun provideChatListViewModel(chatRepo: ChatRepo, userRepo: UserRepo)
            = ChatListViewModel(chatRepo, userRepo)

    @Provides
    fun provideFactory(provider: Provider<ChatListViewModel>)
        = ChatListViewModel.Factory(provider)
}