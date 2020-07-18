package com.example.firebasechatappkotlinmvvm.data.callback


/**
 * Created by Trung on 7/12/2020
 */
interface CallBack<TData, TErrCore> {
    fun onSuccess(data: TData? = null)
    fun onError(errCode: TErrCore)
    fun onFail(errCode: TErrCore)
}