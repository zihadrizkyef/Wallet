package com.zhd.dompet.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager


fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

    val view = this.currentFocus
    if (view != null && imm != null) {
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


}