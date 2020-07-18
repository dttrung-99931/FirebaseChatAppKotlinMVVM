package com.example.firebasechatappkotlinmvvm.data.callback


/**
 * Created by Trung on 7/12/2020
 */
interface SingleCallBack<TData> {
    fun onSuccess(data: TData)
}