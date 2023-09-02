package com.zhd.dompet.activity.login

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.zhd.dompet.R
import com.zhd.dompet.activity.BaseActivity
import com.zhd.dompet.activity.RegisterActivity
import com.zhd.dompet.activity.main.MainActivity
import com.zhd.dompet.databinding.ActivityLoginBinding
import com.zhd.repository.model.User
import com.zhd.repository.repo.UserRepository


class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val repository by lazy { UserRepository() }
    private val adapter: UserAdapter by lazy {
        UserAdapter().apply {
            onClickListener = { _, user ->
                if (user.pin.isNotEmpty()) {
                    showPinDialog(user)
                } else {
                    repository.setUser(user)

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("id", user.id)
                    startActivity(intent)
                }
            }
        }
    }

    private val registerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                Snackbar.make(binding.root, R.string.register_success, Snackbar.LENGTH_LONG).show()
                getData()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        getData()
    }

    private fun setupView() {
        binding.recyclerView.adapter = adapter

        val htmlSpan = SpannableString(
            HtmlCompat.fromHtml(
                getString(R.string.account_not_found_register),
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
        )
        val styleSpan = htmlSpan.getSpans(0, htmlSpan.length, StyleSpan::class.java).first()
        val spanStart = htmlSpan.getSpanStart(styleSpan)
        val spanEnd = htmlSpan.getSpanEnd(styleSpan)
        val clickSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                registerLauncher.launch(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
        htmlSpan.setSpan(clickSpan, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.buttonRegister.text = htmlSpan
        binding.buttonRegister.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun getData() {
        val users = repository.getAllUser()
        adapter.submitList(users)
    }

    private fun showPinDialog(user: User) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.input_pin)
        val viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_input_pin, null, false)
        builder.setView(viewInflated)
        builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
            val inputPassword = viewInflated.findViewById<TextInputEditText>(R.id.inputPin)
            val password = inputPassword.text.toString()
            if (password == user.pin) {
                repository.setUser(user)

                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.putExtra("id", user.id)
                startActivity(intent)
                dialog.dismiss()
            } else {
                inputPassword.error = getString(R.string.wrong_password)
            }
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
        builder.show()
    }
}