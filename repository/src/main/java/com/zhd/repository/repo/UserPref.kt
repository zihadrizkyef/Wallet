package com.zhd.repository.repo

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.gsonpref.gsonNullablePref
import com.zhd.repository.model.User

object UserPref : KotprefModel() {
        var activeUser by gsonNullablePref<User>()
    }