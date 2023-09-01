package com.zhd.dompet.activity.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.zhd.dompet.R
import com.zhd.dompet.activity.BaseActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        repository.createUser("Ayah", "dompetayah")
//        repository.createUser("Bunda", "")

        setupView()
        getData()
    }

    private fun setupView() {
        binding.recyclerView.adapter = adapter
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
            dialog.dismiss()
            val pin = viewInflated.findViewById<TextInputEditText>(R.id.inputPin).text.toString()
            if (pin == user.pin) {
                repository.setUser(user)

                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.putExtra("id", user.id)
                startActivity(intent)
            }
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
        builder.show()
    }
}