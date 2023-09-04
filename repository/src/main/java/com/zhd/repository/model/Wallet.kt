package com.zhd.repository.model

import com.zhd.repository.realmListOf
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Wallet: RealmObject() {
    @PrimaryKey
    var id = 0L
    var userId = 0L

    var name = ""
    var transactions: RealmList<Transaction> = realmListOf()
    var date = Date()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Wallet) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (transactions != other.transactions) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + transactions.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }

}