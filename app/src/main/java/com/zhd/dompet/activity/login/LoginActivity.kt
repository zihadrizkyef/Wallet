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
import com.zhd.dompet.dialog.PasswordDialog
import com.zhd.dompet.utils.Extra
import com.zhd.dompet.utils.ExtraAction
import com.zhd.repository.model.User
import com.zhd.repository.repo.UserRepository


class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val repository by lazy { UserRepository() }
    private val adapter: UserAdapter by lazy {
        UserAdapter().apply {
            onClickListener = { _, user ->
                if (user.pin.isNotEmpty()) {
                    PasswordDialog(this@LoginActivity).show(user.pin) { isSuccess ->
                        if (isSuccess) {
                            repository.setActiveUser(user)
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("id", user.id)
                            mainLauncher.launch(intent)
                        } else {
                            showError(binding.root, R.string.wrong_password)
                        }
                    }
                } else {
                    repository.setActiveUser(user)
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("id", user.id)

                    mainLauncher.launch(intent)
                }
            }
        }
    }

    private val mainLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                if (it.data?.getStringExtra(Extra.ACTION) == ExtraAction.DELETE) {
                    Snackbar.make(binding.root, R.string.delete_account_success, Snackbar.LENGTH_LONG).show()
                }
            }
        }

    private val registerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                Snackbar.make(binding.root, R.string.register_success, Snackbar.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    override fun onResume() {
        super.onResume()

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
}