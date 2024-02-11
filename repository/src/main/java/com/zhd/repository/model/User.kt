package com.zhd.repository.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.UUID

open class User : RealmObject() {
    @PrimaryKey
    var id = UUID.randomUUID().toString()
    var name = ""
    var pin = ""
}