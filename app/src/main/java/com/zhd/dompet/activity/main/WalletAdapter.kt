package com.zhd.dompet.activity.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zhd.dompet.databinding.ItemWalletBinding
import com.zhd.dompet.toCurrency
import com.zhd.repository.model.Wallet

class WalletAdapter : ListAdapter<Wallet, WalletAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Wallet>() {
    override fun areItemsTheSame(oldItem: Wallet, newItem: Wallet): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Wallet, newItem: Wallet): Boolean {
        return oldItem.name == newItem.name && oldItem.transactions == newItem.transactions
    }
}) {

    var onClickListener: (pos: Int, item: Wallet) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWalletBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = with(holder.binding) {
        val item = getItem(position)

        textName.text = item.name
        textBalance.text = item.transactions.sumOf { it.value }.toCurrency()
        root.setOnClickListener {
            onClickListener(position, item)
        }
    }

    class ViewHolder(val binding: ItemWalletBinding) : RecyclerView.ViewHolder(binding.root)
}