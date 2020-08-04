package com.example.firebasechatappkotlinmvvm.ui.chat.chat

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasechatappkotlinmvvm.BR
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.data.callback.SingleCallBack
import com.example.firebasechatappkotlinmvvm.data.repo.chat.Messagee
import com.example.firebasechatappkotlinmvvm.databinding.FragmentChatBinding
import com.example.firebasechatappkotlinmvvm.ui.base.BaseFragment
import com.example.firebasechatappkotlinmvvm.ui.base.OnItemClickListener
import com.example.firebasechatappkotlinmvvm.ui.common.ImageViewerDialog
import com.example.firebasechatappkotlinmvvm.util.AppConstants
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import com.vanniktech.emoji.EmojiPopup
import kotlinx.android.synthetic.main.fragment_chat.*
import javax.inject.Inject

class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>() {
    companion object {
        const val KEY_CHAT_USER = "USER_CHAT_WITH"
    }

    @Inject
    lateinit var mFactory: ChatViewModel.Factory

    override fun getLayoutResId(): Int {
        return R.layout.fragment_chat
    }

    override fun getVMBindingVarId(): Int {
        return BR.viewModel
    }

    override fun getVM(): ChatViewModel {
        return ViewModelProvider(this, mFactory).get(ChatViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.setupChat(arguments?.getParcelable(KEY_CHAT_USER)!!)
    }

    private val onMsgClickListener: OnItemClickListener<Messagee> =
        object : OnItemClickListener<Messagee> {
            override fun onItemClicked(position: Int, itemData: Messagee) {

            }
        }

    private val chatAdapter = ChatAdapter(onMsgClickListener)

    override fun setupViews() {
        setupToolbar()
        setupChatRecyclerView()
        mBtnSend.setOnClickListener {
            vm.onBtnSendClicked()
            mEdtChat.setText("")
        }
        setupEdtChat()
        setupBtnMediaMsgMenu()
        mImgAvatar.setOnClickListener{
            ImageViewerDialog.show(vm.chatUser.avatarUrl, childFragmentManager)
        }
    }

    private fun setupToolbar() {
        mToolbar.setNavigationOnClickListener {
            finishActivity()
        }

        mTvNickname.text = vm.chatUser.nickname

        Glide.with(requireContext())
            .load(vm.chatUser.avatarUrl)
            .placeholder(R.drawable.ic_no_avatar_50px)
            .centerCrop()
            .into(mImgAvatar)
    }

    private fun setupBtnMediaMsgMenu() {
        mBtnMediaMsgMenu.setOnClickListener {
            val mediaMsgMenu = PopupMenu(requireContext(), mBtnMediaMsgMenu)
            mediaMsgMenu.setOnMenuItemClickListener(mOnMediaMsgOptionItemClicwk)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mediaMsgMenu.setForceShowIcon(true)
            }
            mediaMsgMenu.menuInflater.inflate(R.menu.menu_media_msg, mediaMsgMenu.menu)
            mediaMsgMenu.show()
        }
    }

    private val mOnMediaMsgOptionItemClicwk: PopupMenu.OnMenuItemClickListener =
        PopupMenu.OnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_image ->{
                    selectMediaImage(mOnSelectImageResult)
                    return@OnMenuItemClickListener true
                }
                else -> false
            }
        }

    private val mOnSelectImageResult : SingleCallBack<Uri> =
        object : SingleCallBack<Uri> {
            override fun onSuccess(imgUri: Uri) {
                vm.sendImageMessage(openInputStream(imgUri))
            }
        }

    lateinit var mEmojiChoosePopup: EmojiPopup

    private fun setupEdtChat() {
        mEmojiChoosePopup = EmojiPopup.Builder.fromRootView(view).build(mEdtChat)

        mEdtChat.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                if (event!!.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (mEdtChat.right - mEdtChat.compoundDrawables
                                [AppConstants.View.DRAWABLE_RIGHT].bounds.width())
                    ) {
                        mEmojiChoosePopup.toggle()
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun setupChatRecyclerView() {
        val linearLayout = LinearLayoutManager(requireContext())
        mRecyclerView.layoutManager = linearLayout

        // Make recycler view raise messages in bottom up
        // when the keyboard shown
        mRecyclerView.addOnLayoutChangeListener { v, left, top, right, bottom,
                                                  oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom)
                mRecyclerView.postDelayed(Runnable {
                    mRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
                }, 50)
        }
        chatAdapter.meId = vm.meId
        chatAdapter.childFragmentManager = childFragmentManager
        mRecyclerView.setOnScrollListener(onMessageScrollListener)
        mRecyclerView.adapter = chatAdapter
    }

    private val onMessageScrollListener: RecyclerView.OnScrollListener
        = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = recyclerView.layoutManager!! as LinearLayoutManager
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPos = layoutManager.findFirstVisibleItemPosition()

            if (firstVisibleItemPos == 0 &&
                // && it to ignore event when chatAdapter is assigned to mRecyclerView
                totalItemCount >= AppConstants.PAGE_SIZE_MSG &&
                vm.canLoadMoreMsg()) vm.loadMoreMessages()
        }
    }

    override fun observe() {
        vm.onGetCurChatIdSuccess.observe(this, Observer {
            mProgressLoadChat.visibility = View.GONE
            mBtnSend.visibility = View.VISIBLE
        })

        vm.firstMessages.observe(this, Observer {
            chatAdapter.messages = it.toMutableList()
            chatAdapter.notifyDataSetChanged()
            vm.isLoadingRefreshMsgsComplete
            // only scroll to bottom after loading cached msg
            if (!vm.isLoadingRefreshMsgsComplete)
                mRecyclerView.scrollToPosition(chatAdapter.messages.size - 1)
        })

        vm.newMessage.observe(this, Observer {
            chatAdapter.addMessage(it)
            mRecyclerView.scrollToPosition(chatAdapter.messages.size - 1)
        })

        vm.isLoadingMoreMsg.observe(this, Observer {
            if (it) chatAdapter.addLoadMoreProgress()
            else chatAdapter.removeLoadMoreProgress()
        })

        vm.nextMessages.observe(this, Observer {
            chatAdapter.addNextMessages(it)
        })
    }

}