package com.zhd.repository.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User : RealmObject() {
    @PrimaryKey
    var id = 0L

    var name = ""
    var pin = ""
}