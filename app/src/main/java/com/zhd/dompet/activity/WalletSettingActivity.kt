package com.zhd.dompet.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.zhd.dompet.R
import com.zhd.dompet.databinding.ActivityWalletSettingBinding
import com.zhd.dompet.dialog.PasswordDialog
import com.zhd.dompet.utils.Extra
import com.zhd.dompet.utils.ExtraAction
import com.zhd.repository.repo.UserRepository
import com.zhd.repository.repo.WalletRepository

class WalletSettingActivity : BaseActivity() {
    private lateinit var binding: ActivityWalletSettingBinding
    private val repository = WalletRepository()
    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val walletId = intent.getLongExtra("id", 0L)
        val wallet = repository.getSingleById(walletId)
        binding.inputName.setText(wallet.name)
        binding.buttonSave.setOnClickListener {
            val name = binding.inputName.text.toString()
            if (name.isBlank()) {
                showError(binding.root, R.string.name_cannot_empty)
                return@setOnClickListener
            }

            repository.updateWallet(walletId, name)
            val intent = Intent()
            intent.putExtra(Extra.ACTION, ExtraAction.UPDATE)
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.buttonDeleteWallet.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete_wallet_question)
                .setPositiveButton(R.string.yes) { dialog, _ ->
                    if (userRepository.getActiveUser()!!.pin.isNotEmpty()) {
                        PasswordDialog(this).show(userRepository.getActiveUser()!!.pin) { isSuccess ->
                            if (isSuccess) {
                                repository.deleteWallet(walletId)
                                val intent = Intent()
                                intent.putExtra(Extra.ACTION, ExtraAction.DELETE)
                                setResult(RESULT_OK, intent)

                                finish()
                            } else {
                                showError(binding.root, R.string.wrong_password)
                            }
                        }
                    } else {
                        repository.deleteWallet(walletId)
                        val intent = Intent()
                        intent.putExtra(Extra.ACTION, ExtraAction.DELETE)
                        setResult(RESULT_OK, intent)

                        finish()
                    }
                    dialog.dismiss()
                }.setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }
}