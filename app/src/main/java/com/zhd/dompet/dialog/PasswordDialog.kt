package com.zhd.dompet.dialog

import android.content.Context
import android.view.LayoutInflater
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.zhd.dompet.R

class PasswordDialog(private val context: Context) {
    fun show(userPassword: String, listener: (isSuccess: Boolean) -> Unit) {
        val viewInflated = LayoutInflater.from(context).inflate(R.layout.dialog_input_password, null, false)
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.input_password)
            .setView(viewInflated)
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val inputPassword = viewInflated.findViewById<TextInputEditText>(R.id.inputPin)
                val password = inputPassword.text.toString()
                if (password == userPassword) {
                    listener(true)
                } else {
                    inputPassword.error = context.getString(R.string.wrong_password)
                    listener(false)
                }
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .show()
    }
}