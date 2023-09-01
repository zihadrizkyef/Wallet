package com.zhd.repository.repo

import com.zhd.repository.generateUniqueId
import com.zhd.repository.model.Transaction
import com.zhd.repository.model.Wallet
import io.realm.Realm
import java.util.*

class TransactionRepository {
    fun createTransaction(walletId: Long, title: String, desc: String, value: Double) = Realm.getDefaultInstance().executeTransaction { realm ->
        val newId = realm.generateUniqueId(Transaction::class.java)

        val transaction = Transaction().apply {
            id = newId
            this.title = title
            this.note = desc
            this.value = value
            this.date = Date()
        }
        realm.where(Wallet::class.java).equalTo("id", walletId).findFirst()?.transactions?.add(transaction)
    }

    fun getAllTransaction(): List<Transaction> = Realm.getDefaultInstance().let { realm ->
        realm.copyFromRealm(
            realm.where(Transaction::class.java).findAll()
        )
    }

    fun getAllTransactionById(walletId: Long): List<Transaction> = Realm.getDefaultInstance().let { realm ->
        realm.copyFromRealm(
            realm.where(Wallet::class.java).equalTo("id", walletId).findFirst()!!.transactions
        )
    }

    fun getTransactionById(transactionId: Long): Transaction? = Realm.getDefaultInstance().let { realm ->
        realm.where(Transaction::class.java).equalTo("id", transactionId).findFirst()?.let {
            realm.copyFromRealm(it)
        }
    }

    fun getTotalBalance(): Double =
        Realm.getDefaultInstance()
            .where(Transaction::class.java)
            .sum("value")
            .toDouble()

    fun deleteTransaction(id: Long) = Realm.getDefaultInstance().executeTransaction { realm ->
        realm.where(Transaction::class.java)
            .equalTo("id", id)
            .findFirst()
            ?.deleteFromRealm()
    }
}