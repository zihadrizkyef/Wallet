package com.zhd.dompet.activity.transactionlist

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zhd.dompet.R
import com.zhd.dompet.databinding.ItemTransactionBinding
import com.zhd.dompet.utils.toCurrency
import com.zhd.repository.model.Transaction
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

class TransactionAdapter : ListAdapter<Transaction, TransactionAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.value == newItem.value
                && oldItem.date == newItem.date
                && oldItem.title == newItem.title
                && oldItem.note == newItem.note
    }
}) {
    var onClickListener: (Transaction) -> Unit = {}
    private val dateFormatter = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = with(holder.binding) {
        val item = getItem(position)

        textValue.text = item.value.absoluteValue.toCurrency(false)
        textTitle.text = item.title
        textDate.text = dateFormatter.format(item.date)
        iconSymbol.setImageResource(if (item.value >= 0) R.drawable.ic_add else R.drawable.ic_remove)
        iconSymbol.setColorFilter(if (item.value >= 0) Color.GREEN else Color.RED)

        root.setOnClickListener {
            onClickListener(item)
        }
    }

    class ViewHolder(val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root)
}