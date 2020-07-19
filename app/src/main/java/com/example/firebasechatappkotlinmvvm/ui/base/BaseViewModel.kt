package com.example.firebasechatappkotlinmvvm.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


/**
 * Created by Trung on 7/11/2020
 */
open class BaseViewModel: ViewModel() {
    val onError = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

}