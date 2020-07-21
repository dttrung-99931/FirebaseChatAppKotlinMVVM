package com.example.firebasechatappkotlinmvvm.util.extension

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


/**
 * Created by Trung on 7/21/2020
 */
class ExtentionUI {
}

fun EditText.showKeyBoard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE)
            as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun EditText.hideKeyboard() {
    val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE)
            as InputMethodManager
    manager.hideSoftInputFromWindow(
        windowToken,
        InputMethodManager.RESULT_UNCHANGED_SHOWN
    )
}
