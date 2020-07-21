package com.example.firebasechatappkotlinmvvm.ui.base


/**
 * Created by Trung on 7/21/2020
 */
interface OnItemClickListener<TData> {
    fun onItemClicked(position: Int, itemData: TData)
}