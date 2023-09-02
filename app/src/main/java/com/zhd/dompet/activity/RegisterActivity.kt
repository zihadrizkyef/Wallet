package com.zhd.dompet.activity

import android.os.Bundle
import com.zhd.dompet.R
import com.zhd.dompet.databinding.ActivityRegisterBinding
import com.zhd.repository.repo.UserRepository

class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val repository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSave.setOnClickListener {
            val name = binding.inputName.text.toString()
            val password = binding.inputPassword.text.toString()

            if (name.isBlank()) {
                showError(binding.root, R.string.name_cannot_empty)
                return@setOnClickListener
            }
            repository.createUser(name, password)

            setResult(RESULT_OK)
            finish()
        }
    }
}