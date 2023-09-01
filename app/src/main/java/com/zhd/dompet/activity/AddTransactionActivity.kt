package com.zhd.dompet.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import com.zhd.dompet.NumeralTextWatcher
import com.zhd.dompet.R
import com.zhd.dompet.databinding.ActivityAddTransactionBinding
import com.zhd.dompet.fromCurrency
import com.zhd.repository.repo.TransactionRepository
import com.zhd.repository.repo.WalletRepository

class AddTransactionActivity : BaseActivity() {
    private lateinit var binding: ActivityAddTransactionBinding
    private val repository by lazy { TransactionRepository() }
    private val walletRepository by lazy { WalletRepository() }
    private var symbol = "+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val walletId = intent.getLongExtra("id", 0L)
        binding.textWalletName.text = walletRepository.getSingleById(walletId).name

        binding.buttonSymbol.setOnClickListener {
            if (symbol == "+") {
                symbol = "-"
                binding.buttonSymbol.backgroundTintList = ColorStateList.valueOf(Color.RED)
                binding.buttonSymbol.setImageResource(R.drawable.ic_remove)
            } else {
                symbol = "+"
                binding.buttonSymbol.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
                binding.buttonSymbol.setImageResource(R.drawable.ic_add)
            }
        }

        binding.inputValue.addTextChangedListener(NumeralTextWatcher(binding.inputValue))
        binding.buttonSave.setOnClickListener {
            repository.createTransaction(
                walletId,
                binding.inputTitle.text.toString(),
                binding.inputNote.text.toString(),
                (symbol + binding.inputValue.text.toString().fromCurrency()).toDouble(),
            )
            finish()
        }
    }
}