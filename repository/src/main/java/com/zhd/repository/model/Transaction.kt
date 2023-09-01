package com.zhd.repository.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Transaction: RealmObject() {

    @PrimaryKey
    var id = 0L

    var title = ""
    var note = ""
    var value = 0.0
    var date = Date()
}