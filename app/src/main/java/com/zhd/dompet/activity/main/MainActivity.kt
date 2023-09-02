package com.zhd.dompet.activity.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.zhd.dompet.R
import com.zhd.dompet.activity.AddWalletActivity
import com.zhd.dompet.activity.BaseActivity
import com.zhd.dompet.activity.SettingActivity
import com.zhd.dompet.activity.transactionlist.TransactionListActivity
import com.zhd.dompet.databinding.ActivityMainBinding
import com.zhd.dompet.utils.Extra
import com.zhd.dompet.utils.ExtraAction
import com.zhd.dompet.utils.toCurrency
import com.zhd.repository.repo.TransactionRepository
import com.zhd.repository.repo.WalletRepository


class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    private val repository by lazy { WalletRepository() }
    private val transactionRepository by lazy { TransactionRepository() }
    private val adapter: WalletAdapter by lazy {
        WalletAdapter().apply {
            onClickListener = { _, item ->
                val intent = Intent(this@MainActivity, TransactionListActivity::class.java)
                intent.putExtra("id", item.id)
                walletDetailLauncher.launch(intent)
            }
        }
    }

    private val addWalletLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        getData()
    }
    private val walletDetailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        getData()
    }
    private val settingLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            if (it.data?.getStringExtra(Extra.ACTION) == ExtraAction.DELETE) {
                setResult(RESULT_OK, it.data)
                finish()
            } else if (it.data?.getStringExtra(Extra.ACTION) == ExtraAction.UPDATE) {
                showSuccess(binding.root, R.string.update_success)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        getData()
    }

    private fun setupView() {
        binding.recyclerView.adapter = adapter

        binding.buttonSetting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            settingLauncher.launch(intent)
        }
        binding.buttonAdd.setOnClickListener {
            val intent = Intent(this, AddWalletActivity::class.java)
            addWalletLauncher.launch(intent)
        }
    }

    private fun getData() {
        val wallets = repository.getUserWallets()
        adapter.submitList(wallets)
        binding.recyclerView.isVisible = wallets.isNotEmpty()
        binding.textNoItem.isVisible = wallets.isEmpty()

        binding.textValueTotalBalance.text = repository.getUserTotalBalance().toCurrency()
    }
}