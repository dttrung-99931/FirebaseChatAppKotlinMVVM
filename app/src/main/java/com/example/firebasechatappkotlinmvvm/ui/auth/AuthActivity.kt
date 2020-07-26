package com.example.firebasechatappkotlinmvvm.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.ui.base.BaseActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject


/**
 * Created by Trung on 7/11/2020
 */

class AuthActivity : BaseActivity(), HasSupportFragmentInjector {
    @Inject
    internal lateinit var mFragmentInjector: DispatchingAndroidInjector<Fragment>

    companion object{
        fun open(context: Context) {
            val intent = Intent(context, AuthActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_auth)
        setupViews()
    }

    private fun setupViews() {
        setupViewPager()
    }

    private fun setupViewPager() {
        mViewPager.adapter =
            AuthFramentPagerAdapter(supportFragmentManager, this)
        mTabLayout.setupWithViewPager(mViewPager)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return mFragmentInjector
    }

}