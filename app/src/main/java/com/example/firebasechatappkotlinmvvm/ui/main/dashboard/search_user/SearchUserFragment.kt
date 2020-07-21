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
    }

    val onSearchUserClickedCallBack: OnItemClickListener<AppUser> =
        object : OnItemClickListener<AppUser> {
            override fun onItemClicked(position: Int, user: AppUser) {
                showToastMsg(user.nickname)
            }
        }

    val mSearchUserAdapter = SearchUserResultAdapter(onSearchUserClickedCallBack)

    private fun setupSearchResultRecyclerView() {
        mRecyclerView.adapter = mSearchUserAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupEdtSearch() {
        mEdtSearch.requestFocus()
        mEdtSearch.showKeyBoard()
        mEdtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                vm.onSearchTextChanged()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

    }

    override fun observe() {
        vm.searchUsersResult.observe(this, Observer {
            showSearchUserResult(it)
        })
    }

    private fun showSearchUserResult(it: List<AppUser>?) {
        mSearchUserAdapter.searchUsers = it!!
        mSearchUserAdapter.notifyDataSetChanged()
    }
}