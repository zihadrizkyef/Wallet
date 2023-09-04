package com.zhd.repository

import io.realm.RealmList

fun <T> realmListOf(vararg list: T) = RealmList<T>().apply {
    if (list.isNotEmpty()) addAll(list)
}