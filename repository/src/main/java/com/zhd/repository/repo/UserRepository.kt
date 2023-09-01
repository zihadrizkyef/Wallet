package com.zhd.repository.repo

import com.zhd.repository.generateUniqueId
import com.zhd.repository.model.User
import io.realm.Realm

class UserRepository {
    fun createUser(name: String, pin: String) = Realm.getDefaultInstance().executeTransaction { realm ->
        val newId = realm.generateUniqueId(User::class.java)

        val user = User().apply {
            id = newId
            this.name = name
            this.pin = pin
        }

        realm.insertOrUpdate(user)
    }

    fun getAllUser(): List<User> = Realm.getDefaultInstance().let { realm ->
        realm.copyFromRealm(
            realm.where(User::class.java).findAll()
        )
    }

    fun setUser(user: User) {
        UserPref.activeUser = user
    }


    fun deleteUser(id: Long) = Realm.getDefaultInstance().executeTransaction { realm ->
        realm.where(User::class.java)
            .equalTo("id", id)
            .findFirst()
            ?.deleteFromRealm()
    }
}