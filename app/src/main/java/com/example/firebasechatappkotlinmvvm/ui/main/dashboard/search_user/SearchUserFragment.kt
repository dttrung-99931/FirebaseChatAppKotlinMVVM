package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.search_user

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasechatappkotlinmvvm.BR
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.databinding.FragmentSearchUserBinding
import com.example.firebasechatappkotlinmvvm.ui.base.BaseFragment
import com.example.firebasechatappkotlinmvvm.ui.base.OnItemClickListener
import com.example.firebasechatappkotlinmvvm.ui.base.OnItemWithPositionClickListener
import com.example.firebasechatappkotlinmvvm.ui.base.OptionBottomSheetDialogFragment
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.chat.ChatFragment
import com.example.firebasechatappkotlinmvvm.util.AppConstants
import com.example.firebasechatappkotlinmvvm.util.extension.hideKeyboard
import com.example.firebasechatappkotlinmvvm.util.extension.showKeyBoard
import kotlinx.android.synthetic.main.fragment_search_user.*
import javax.inject.Inject

class SearchUserFragment : BaseFragment<FragmentSearchUserBinding, SearchUserViewModel>() {
    @Inject
    lateinit var mFactory: SearchUserViewModel.Factory

    override fun getLayoutResId(): Int {
        return R.layout.fragment_search_user
    }

    override fun getVMBindingVarId(): Int {
        return BR.viewModel;
    }

    override fun getVM(): SearchUserViewModel {
        return ViewModelProviders
            .of(this, mFactory)[SearchUserViewModel::class.java]
    }

    override fun setupViews() {
        setupEdtSearch()
        setupSearchResultRecyclerView()
        mBtnBack.setOnClickListener {
            mEdtSearch.hideKeyboard()
            popBackFragment()
        }
    }

    private val onSearchUserClickedCallBack: OnItemClickListener<AppUser> =
        object : OnItemClickListener<AppUser> {
            override fun onItemClicked(position: Int, user: AppUser) {
                showUserOptionBottomSheetOnClickSearch(position, user)
            }
        }

    private fun showUserOptionBottomSheetOnClickSearch(
        position: Int,
        user: AppUser
    ) {
        OptionBottomSheetDialogFragment(
            AppConstants.STR_IDS_CLICK_SEARCH_USER_OPTION,
            object : OnItemWithPositionClickListener {
                override fun onItemWithPositionClicked(position: Int) {
                    when (position) {
                        0 -> navigateWithSerializableData(
                                R.id.action_searchUserFragment_to_chatFragment,
                                user, ChatFragment.KEY_USER_CHAT_WITH)
                    }
                }
            }
        ).show(childFragmentManager, "")
    }

    private val mSearchUserAdapter = SearchUserResultAdapter(onSearchUserClickedCallBack)

    private fun setupSearchResultRecyclerView() {
        mRecyclerView.adapter = mSearchUserAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupEdtSearch() {
        mEdtSearch.requestFocus()
        mEdtSearch.showKeyBoard()
        mEdtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vm.onSearchTextChanged()
            }
        })

    }

    override fun observe() {
        vm.searchUsers.observe(this, Observer {
            showSearchUserResult(it)
        })
    }

    private fun showSearchUserResult(it: List<AppUser>?) {
        mSearchUserAdapter.searchUsers = it!!
        mSearchUserAdapter.notifyDataSetChanged()
    }
}