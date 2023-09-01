package com.zhd.dompet.activity

import android.os.Bundle
import com.zhd.dompet.databinding.ActivityAddWalletBinding
import com.zhd.repository.repo.WalletRepository

class AddWalletActivity : BaseActivity() {
    private lateinit var binding: ActivityAddWalletBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSave.setOnClickListener {
            WalletRepository().createWallet(
                binding.inputName.text.toString(),
            )

            finish()
        }
    }
}