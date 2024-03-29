package com.zhd.repository.repo

import com.zhd.repository.model.Transaction
import com.zhd.repository.model.Wallet
import io.realm.Realm
import java.util.*

class TransactionRepository {
    fun createTransaction(walletId: String, title: String, desc: String, value: Double) =
        Realm.getDefaultInstance().executeTransaction { realm ->
            val transaction = Transaction().apply {
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

    fun getTransactionsFiltered(walletId: String, startDate: Date?, endDate: Date?): List<Transaction> =
        Realm.getDefaultInstance().let { realm ->
            var list = realm.copyFromRealm(
                realm.where(Wallet::class.java)
                    .equalTo("id", walletId)
                    .findFirst()!!
                    .transactions
            )
            if (startDate != null) {
                list = list.filter {
                    it.date > startDate
                }
            }
            if (endDate != null) {
                list = list.filter {
                    it.date < endDate
                }
            }
            list
        }

    fun getTransactionById(transactionId: String): Transaction? = Realm.getDefaultInstance().let { realm ->
        realm.where(Transaction::class.java).equalTo("id", transactionId).findFirst()?.let {
            realm.copyFromRealm(it)
        }
    }

    fun deleteTransaction(id: String) = Realm.getDefaultInstance().executeTransaction { realm ->
        realm.where(Transaction::class.java)
            .equalTo("id", id)
            .findFirst()
            ?.deleteFromRealm()
    }
}