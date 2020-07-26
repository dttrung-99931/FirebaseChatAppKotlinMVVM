package com.example.firebasechatappkotlinmvvm.ui.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.data.repo.chat.ChatUser
import com.example.firebasechatappkotlinmvvm.ui.base.BaseActivity
import com.example.firebasechatappkotlinmvvm.ui.chat.chat.ChatFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class ChatActivity : BaseActivity(), HasSupportFragmentInjector {
    @Inject
    internal lateinit var mFragmentInjector: DispatchingAndroidInjector<Fragment>
    companion object{
        const val KEY_CHAT_USER_BUNDLE = "KEY_CHAT_USER_BUNDLE"

        fun open(context: Context, chatUser: ChatUser) {
            val intent = Intent(context, ChatActivity::class.java)
            val chatUserBundle = bundleOf(ChatFragment.KEY_CHAT_USER to chatUser)
            intent.putExtra(KEY_CHAT_USER_BUNDLE, chatUserBundle)
            context.startActivity(intent, chatUserBundle)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_chat)
        passChatUserParamToChatFragment()
    }

    // call navController.setGraph to pass chatUser to
    // ChatFragment (that is home destination in nav_chat)
    private fun passChatUserParamToChatFragment() {
        val navController = findNavController(R.id.nav_host_fragment)
        val chatUserBundle = intent.getBundleExtra(KEY_CHAT_USER_BUNDLE)
        navController.setGraph(navController.graph, chatUserBundle)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return mFragmentInjector
    }

}