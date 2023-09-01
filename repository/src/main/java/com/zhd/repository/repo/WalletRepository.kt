package com.zhd.repository.repo

import com.zhd.repository.generateUniqueId
import com.zhd.repository.model.Wallet
import io.realm.Realm

class WalletRepository {
    fun createWallet(name: String, pin: String) = Realm.getDefaultInstance().executeTransaction { realm ->
        val newId = realm.generateUniqueId(Wallet::class.java)

        val wallet = Wallet().apply {
            id = newId
            this.name = name
        }

        realm.insertOrUpdate(wallet)
    }

    fun getAllWallet(): List<Wallet> = Realm.getDefaultInstance().let { realm ->
        realm.copyFromRealm(
            realm.where(Wallet::class.java).findAll()
        )
    }

    fun getById(id: Long): Wallet = Realm.getDefaultInstance().let { realm ->
        realm.copyFromRealm(
            realm.where(Wallet::class.java).equalTo("id", id).findFirst()!!
        )
    }

    fun deleteWallet(id: Long) = Realm.getDefaultInstance().executeTransaction { realm ->
        realm.where(Wallet::class.java)
            .equalTo("id", id)
            .findFirst()
            ?.deleteFromRealm()
    }
}