package com.zhd.repository

import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmModel

fun <T> realmListOf(vararg list: T) = RealmList<T>().apply {
    if (list.isNotEmpty()) addAll(list)
}

fun <E : RealmModel> Realm.generateUniqueId(clazz: Class<E>): Long {
    return (this.where(clazz).max("id")?.toLong() ?: 0L) + 1
}