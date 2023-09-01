package com.zhd.repository.model

import com.zhd.repository.realmListOf
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Wallet: RealmObject() {
    @PrimaryKey
    var id = 0L

    var name = ""
    var transactions: RealmList<Transaction> = realmListOf()
    var date = Date()
    var pin = ""

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Wallet) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (transactions != other.transactions) return false
        if (date != other.date) return false
        if (pin != other.pin) return false

        return true
    }

}