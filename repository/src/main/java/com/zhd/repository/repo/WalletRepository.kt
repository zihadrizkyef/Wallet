package com.zhd.repository.repo

import com.zhd.repository.generateUniqueId
import com.zhd.repository.model.Transaction
import com.zhd.repository.model.Wallet
import io.realm.Realm

class WalletRepository {
    fun createWallet(name: String) = Realm.getDefaultInstance().executeTransaction { realm ->
        val newId = realm.generateUniqueId(Wallet::class.java)

        val wallet = Wallet().apply {
            id = newId
            this.userId = UserPref.activeUser!!.id
            this.name = name
        }

        realm.insertOrUpdate(wallet)
    }

    fun getUserWallets(): List<Wallet> = Realm.getDefaultInstance().let { realm ->
        realm.copyFromRealm(
            realm.where(Wallet::class.java).equalTo("userId", UserPref.activeUser!!.id).findAll()
        )
    }

    fun getSingleById(id: Long): Wallet = Realm.getDefaultInstance().let { realm ->
        realm.copyFromRealm(
            realm.where(Wallet::class.java).equalTo("id", id).findFirst()!!
        )
    }

    fun getUserTotalBalance(): Double {
        val wallets = Realm.getDefaultInstance()
            .where(Wallet::class.java)
            .equalTo("userId", UserPref.activeUser!!.id)
            .findAll()

        val totalBalance = wallets.sumOf { wallet ->
            wallet.transactions.sumOf { transaction -> transaction.value }
        }

        return totalBalance
    }

    fun deleteWallet(id: Long) = Realm.getDefaultInstance().executeTransaction { realm ->
        realm.where(Wallet::class.java)
            .equalTo("id", id)
            .findFirst()
            ?.deleteFromRealm()
    }
}