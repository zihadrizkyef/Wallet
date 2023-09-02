package com.zhd.dompet.activity

import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    fun showError(view: View, text: String) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show()
    }

    fun showError(view: View, @StringRes resId: Int) {
        showError(view, getString(resId))
    }

    fun showSuccess(view: View, text: String) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show()
    }

    fun showSuccess(view: View, @StringRes resId: Int) {
        showSuccess(view, getString(resId))
    }

}