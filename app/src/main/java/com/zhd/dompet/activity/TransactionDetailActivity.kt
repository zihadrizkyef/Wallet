package com.zhd.dompet.activity

import android.os.Bundle
import com.zhd.dompet.databinding.ActivityTransactionDetailBinding
import com.zhd.dompet.utils.toCurrency
import com.zhd.repository.repo.TransactionRepository

class TransactionDetailActivity : BaseActivity() {
    private val repository by lazy { TransactionRepository() }
    private lateinit var binding: ActivityTransactionDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val transactionId = intent.getLongExtra("id", 0L)
        val transaction = repository.getTransactionById(transactionId)
        transaction?.let {
            binding.textValue.text = it.value.toCurrency()
            binding.textTitle.text = it.title
            binding.textNote.text = it.note
            binding.buttonDelete.setOnClickListener {
                repository.deleteTransaction(transactionId)
                finish()
            }
        }
    }

}
