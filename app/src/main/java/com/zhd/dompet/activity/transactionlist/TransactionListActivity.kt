package com.zhd.dompet.activity.transactionlist

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.zhd.dompet.activity.AddTransactionActivity
import com.zhd.dompet.activity.BaseActivity
import com.zhd.dompet.activity.TransactionDetailActivity
import com.zhd.dompet.databinding.ActivityTransactionListBinding
import com.zhd.dompet.toCurrency
import com.zhd.repository.repo.TransactionRepository
import com.zhd.repository.repo.WalletRepository

class TransactionListActivity : BaseActivity() {
    private lateinit var binding: ActivityTransactionListBinding
    private val adapter: TransactionAdapter by lazy {
        TransactionAdapter().apply {
            onClickListener = {
                val intent = Intent(this@TransactionListActivity, TransactionDetailActivity::class.java)
                intent.putExtra("id", it.id)
                detailTransactionLauncher.launch(intent)
            }
        }
    }
    private val repository by lazy { TransactionRepository() }
    private val walletRepository by lazy { WalletRepository() }
    private var walletId = 0L

    private val addTransactionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        getData()
    }

    private val detailTransactionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        getData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        walletId = intent.getLongExtra("id", 0)
        binding.recyclerView.adapter = adapter
        getData()

        binding.buttonAdd.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            intent.putExtra("id", walletId)
            addTransactionLauncher.launch(intent)
        }
    }

    private fun getData() {
        val transactions = repository.getTransactionsByWalletId(walletId)
        adapter.submitList(transactions)
        binding.recyclerView.isVisible = transactions.isNotEmpty()
        binding.textNoItem.isVisible = transactions.isEmpty()
        binding.textValueTotalBalance.text = transactions.sumOf { it.value }.toCurrency()
        binding.textWalletName.text = walletRepository.getSingleById(walletId).name
    }
}