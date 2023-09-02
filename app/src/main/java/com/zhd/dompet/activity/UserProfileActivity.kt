package com.zhd.dompet.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.zhd.dompet.R
import com.zhd.dompet.databinding.ActivityUserProfileBinding
import com.zhd.dompet.dialog.PasswordDialog
import com.zhd.dompet.utils.Extra
import com.zhd.dompet.utils.ExtraAction
import com.zhd.repository.repo.UserRepository

class UserProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private val repository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
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
                    if (repository.getActiveUser()!!.pin.isNotEmpty()) {
                        PasswordDialog(this).show(repository.getActiveUser()!!.pin) { isSuccess ->
                            if (isSuccess) {
                                repository.deleteActiveUser()
                                val intent = Intent()
                                intent.putExtra(Extra.ACTION, ExtraAction.DELETE)
                                setResult(RESULT_OK, intent)

                                finish()
                            } else {
                                showError(binding.root, R.string.wrong_password)
                            }
                        }
                    } else {
                        repository.deleteActiveUser()
                        val intent = Intent()
                        intent.putExtra(Extra.ACTION, ExtraAction.DELETE)
                        setResult(RESULT_OK, intent)

                        finish()
                    }
                }.setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }
}