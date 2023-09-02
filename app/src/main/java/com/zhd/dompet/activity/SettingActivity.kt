package com.zhd.dompet.activity

import android.content.Intent
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zhd.dompet.R
import com.zhd.dompet.databinding.ActivitySettingBinding
import com.zhd.dompet.utils.Extra
import com.zhd.dompet.utils.ExtraAction
import com.zhd.repository.repo.UserRepository

class SettingActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingBinding
    private val repository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.inputName.setText(repository.getActiveUser()?.name ?: "")
        binding.inputPassword.setText(repository.getActiveUser()?.pin ?: "")
        binding.buttonSave.setOnClickListener {
            val name = binding.inputName.text.toString()
            if (name.isBlank()) {
                showError(binding.root, R.string.name_cannot_empty)
                return@setOnClickListener
            }

            val password = binding.inputPassword.text.toString()
            repository.updateActiveUser(name, password)

            val intent = Intent()
            intent.putExtra(Extra.ACTION, ExtraAction.UPDATE)
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.buttonDeleteAccount.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete_account_question)
                .setPositiveButton(R.string.yes) { _, _ ->
                    repository.deleteActiveUser()
                    val intent = Intent()
                    intent.putExtra(Extra.ACTION, ExtraAction.DELETE)
                    setResult(RESULT_OK, intent)

                    finish()
                }.setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }
}