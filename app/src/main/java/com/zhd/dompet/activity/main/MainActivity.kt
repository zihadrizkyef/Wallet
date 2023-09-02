package com.zhd.dompet.activity.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.zhd.dompet.R
import com.zhd.dompet.activity.AddWalletActivity
import com.zhd.dompet.activity.BaseActivity
import com.zhd.dompet.activity.UserProfileActivity
import com.zhd.dompet.activity.transactionlist.TransactionListActivity
import com.zhd.dompet.databinding.ActivityMainBinding
import com.zhd.dompet.utils.Extra
import com.zhd.dompet.utils.ExtraAction
import com.zhd.dompet.utils.toCurrency
import com.zhd.repository.repo.UserRepository
import com.zhd.repository.repo.WalletRepository


class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    private val repository = WalletRepository()
    private val userRepository = UserRepository()

    private val adapter: WalletAdapter by lazy {
        WalletAdapter().apply {
            onClickListener = { _, item ->
                val intent = Intent(this@MainActivity, TransactionListActivity::class.java)
                intent.putExtra("id", item.id)
                walletLauncher.launch(intent)
            }
        }
    }

    private val walletLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                if (it.data?.getStringExtra(Extra.ACTION) == ExtraAction.DELETE) {
                    showSuccess(binding.root, R.string.delete_wallet_success)
                }
            }
        }

    private val profileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            if (it.data?.getStringExtra(Extra.ACTION) == ExtraAction.DELETE) {
                setResult(RESULT_OK, it.data)
                finish()
            } else if (it.data?.getStringExtra(Extra.ACTION) == ExtraAction.UPDATE) {
                getData()
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

    override fun onResume() {
        super.onResume()

        getData()
    }

    private fun setupView() {
        binding.textTitle.text = getString(R.string.hi_n, userRepository.getActiveUser()?.name)

        binding.recyclerView.adapter = adapter

        binding.buttonSetting.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            profileLauncher.launch(intent)
        }
        binding.buttonAdd.setOnClickListener {
            val intent = Intent(this, AddWalletActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getData() {
        binding.textTitle.text = getString(R.string.hi_n, userRepository.getActiveUser()?.name)

        val wallets = repository.getUserWallets()
        adapter.submitList(wallets)
        binding.recyclerView.isVisible = wallets.isNotEmpty()
        binding.textNoItem.isVisible = wallets.isEmpty()

        binding.textValueTotalBalance.text = repository.getUserTotalBalance().toCurrency()
    }
}