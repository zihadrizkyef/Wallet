package com.zhd.dompet.activity.transactionlist

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.zhd.dompet.activity.*
import com.zhd.dompet.databinding.ActivityTransactionListBinding
import com.zhd.dompet.utils.Extra
import com.zhd.dompet.utils.ExtraAction
import com.zhd.dompet.utils.toCurrency
import com.zhd.repository.repo.TransactionRepository
import com.zhd.repository.repo.WalletRepository

class TransactionListActivity : BaseActivity() {
    private lateinit var binding: ActivityTransactionListBinding
    private val adapter: TransactionAdapter by lazy {
        TransactionAdapter().apply {
            onClickListener = {
                val intent = Intent(this@TransactionListActivity, TransactionDetailActivity::class.java)
                intent.putExtra("id", it.id)
                listUpdateLauncher.launch(intent)
            }
        }
    }
    private val repository by lazy { TransactionRepository() }
    private val walletRepository by lazy { WalletRepository() }
    private var walletId = 0L

    private val listUpdateLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            getData()
        }

    private val editorLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                if (it.data?.getStringExtra(Extra.ACTION) == ExtraAction.DELETE) {
                    setResult(RESULT_OK, it.data)
                    finish()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        walletId = intent.getLongExtra("id", 0)
        setupView()
        getData()
    }

    private fun setupView() {
        binding.recyclerView.adapter = adapter

        binding.buttonSetting.setOnClickListener {
            val intent = Intent(this, WalletSettingActivity::class.java)
            intent.putExtra("id", walletId)
            editorLauncher.launch(intent)
        }

        binding.buttonAdd.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            intent.putExtra("id", walletId)
            listUpdateLauncher.launch(intent)
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