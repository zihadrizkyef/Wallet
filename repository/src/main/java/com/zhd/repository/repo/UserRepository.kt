package com.zhd.repository.repo

import com.zhd.repository.model.User
import io.realm.Realm

class UserRepository {

    fun createUser(name: String, pin: String) = Realm.getDefaultInstance().executeTransaction { realm ->
        val user = User().apply {
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

    fun updateActiveUser(name: String, password: String) = Realm.getDefaultInstance().executeTransaction { realm ->
        val activeUser = UserPref.activeUser!!.apply {
            this.name = name
            this.pin = password
        }
        realm.insertOrUpdate(activeUser)
        val updatedActiveUser = realm.where(User::class.java)
            .equalTo("id", activeUser.id)
            .findFirst()!!
        setActiveUser(realm.copyFromRealm(updatedActiveUser))
    }

    fun setActiveUser(user: User) {
        UserPref.activeUser = user
    }

    fun deleteActiveUser() = deleteUser(UserPref.activeUser!!.id)

    fun getActiveUser() = UserPref.activeUser

    fun deleteUser(id: String) = Realm.getDefaultInstance().executeTransaction { realm ->
        realm.where(User::class.java)
            .equalTo("id", id)
            .findFirst()
            ?.deleteFromRealm()
    }
}