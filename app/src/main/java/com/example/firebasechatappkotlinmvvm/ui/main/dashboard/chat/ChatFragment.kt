package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.chat

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasechatappkotlinmvvm.BR
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.data.repo.chat.ChatUser
import com.example.firebasechatappkotlinmvvm.data.repo.chat.Messagee
import com.example.firebasechatappkotlinmvvm.databinding.FragmentChatBinding
import com.example.firebasechatappkotlinmvvm.ui.base.BaseFragment
import com.example.firebasechatappkotlinmvvm.ui.base.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_search_user.mRecyclerView
import javax.inject.Inject

class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>() {
    companion object{
        const val KEY_CHAT_USER = "USER_CHAT_WITH"
    }

    @Inject
    lateinit var mFactory: ChatViewModel.Factory

    override fun getLayoutResId(): Int {
        return R.layout.fragment_chat
    }

    override fun getVMBindingVarId(): Int {
        return BR.viewModel;
    }

    override fun getVM(): ChatViewModel {
        return ViewModelProviders
            .of(this, mFactory)[ChatViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.setupChat(arguments?.getParcelable(KEY_CHAT_USER)!!)
    }

    val onMsgClickListener : OnItemClickListener<Messagee> =
        object : OnItemClickListener<Messagee> {
            override fun onItemClicked(position: Int, itemData: Messagee) {

            }
        }

    private val chatAdapter = ChatAdapter(onMsgClickListener)

    override fun setupViews() {
        setupChatRecyclerView()
        mBtnSend.setOnClickListener {
            vm.onBtnSendClicked()
            mEdtChat.setText("")
        }
    }

    private fun setupChatRecyclerView() {
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        chatAdapter.meId = vm.meId
        mRecyclerView.adapter = chatAdapter
    }

    override fun observe() {
        vm.messages.observe(this, Observer {
            chatAdapter.messages = it.toMutableList()
            chatAdapter.notifyDataSetChanged()
            mRecyclerView.scrollToPosition(chatAdapter.messages.size-1)
        })

        vm.newMessage.observe(this, Observer {
            chatAdapter.addMessage(it)
            mRecyclerView.scrollToPosition(chatAdapter.messages.size-1)
        })
    }

}