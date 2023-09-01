package com.zhd.dompet.activity.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputEditText
import com.zhd.dompet.R
import com.zhd.dompet.activity.AddWalletActivity
import com.zhd.dompet.activity.BaseActivity
import com.zhd.dompet.activity.transactionlist.TransactionListActivity
import com.zhd.dompet.databinding.ActivityMainBinding
import com.zhd.dompet.toCurrency
import com.zhd.repository.model.Wallet
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        getData()
    }

    private fun setupView() {
        binding.recyclerView.adapter = adapter

        binding.buttonAdd.setOnClickListener {
            val intent = Intent(this, AddWalletActivity::class.java)
            addWalletLauncher.launch(intent)
        }
    }

    private fun getData() {
        val wallets = repository.getAllWallet()
        adapter.submitList(wallets)
        binding.recyclerView.isVisible = wallets.isNotEmpty()
        binding.textNoItem.isVisible = wallets.isEmpty()

        binding.textValueTotalBalance.text = transactionRepository.getTotalBalance().toCurrency()
    }

    private fun showPinDialog(wallet: Wallet) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.input_pin)
        val viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_input_pin, null, false)
        builder.setView(viewInflated)
        builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
            dialog.dismiss()
            val pin = viewInflated.findViewById<TextInputEditText>(R.id.inputPin).text.toString()
            if (pin == wallet.pin) {

            }
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, which -> dialog.cancel() }
        builder.show()
    }
}