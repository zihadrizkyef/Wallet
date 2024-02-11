package com.zhd.dompet.activity.transactionlist

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.zhd.dompet.R
import com.zhd.dompet.activity.*
import com.zhd.dompet.databinding.ActivityTransactionListBinding
import com.zhd.dompet.dialog.FilterCalendarDialog
import com.zhd.dompet.utils.Extra
import com.zhd.dompet.utils.ExtraAction
import com.zhd.dompet.utils.toCurrency
import com.zhd.repository.repo.TransactionRepository
import com.zhd.repository.repo.WalletRepository
import java.text.SimpleDateFormat
import java.util.*

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
    private var walletId = ""
    private var filterStartDate: Date? = null
    private var filterEndDate: Date? = null

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

        walletId = intent.getStringExtra("id")!!
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

        binding.buttonCalendar.setOnClickListener {
            showCalendarFilter()
        }

        binding.buttonAdd.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            intent.putExtra("id", walletId)
            listUpdateLauncher.launch(intent)
        }
    }

    private fun getData() {
        val transactions = repository.getTransactionsFiltered(walletId, filterStartDate, filterEndDate)

        adapter.submitList(transactions)
        binding.recyclerView.isVisible = transactions.isNotEmpty()
        binding.textNoItem.isVisible = transactions.isEmpty()
        binding.textValueTotalBalance.text = transactions.sumOf { it.value }.toCurrency()
        binding.textWalletName.text = walletRepository.getSingleById(walletId).name
    }

    private fun showCalendarFilter() {
        val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

        val dialog = FilterCalendarDialog()
        dialog.dateStart = filterStartDate
        dialog.dateEnd = filterEndDate
        dialog.onShowAllListener = {
            binding.buttonCalendar.setText(R.string.show_all)
            filterStartDate = null
            filterEndDate = null
            getData()
        }
        dialog.onSaveListener = { dateStart, dateEnd ->
            if (dateStart != null && dateEnd != null) {
                binding.buttonCalendar.text = dateFormat.format(dateStart) + " - " + dateFormat.format(dateEnd)
            } else if (dateStart != null) {
                binding.buttonCalendar.text = getString(R.string.from_n, dateFormat.format(dateStart))
            } else if (dateEnd != null) {
                binding.buttonCalendar.text = getString(R.string.until_n, dateFormat.format(dateEnd))
            }
            filterStartDate = dateStart
            filterEndDate = dateEnd
            getData()
        }
        dialog.show(supportFragmentManager, null)
    }
}