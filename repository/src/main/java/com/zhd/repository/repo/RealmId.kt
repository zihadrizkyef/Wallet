package com.zhd.repository.repo

import com.chibatching.kotpref.KotprefModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.realm.RealmModel

object RealmId : KotprefModel() {
    private var idMapJson by stringPref("{}")
    private val gson = Gson()

    fun <E : RealmModel> generateUniqueId(clazz: Class<E>): Long {
        val packageAndName = getClassPackageAndName(clazz)
        val idMap = getIdMap()
        val find = idMap[packageAndName]
        return if (find == null) {
            idMap[packageAndName] = 0
            setIdMap(idMap)
            0
        } else {
            idMap[packageAndName] = idMap[packageAndName]!! + 1
            setIdMap(idMap)
            find + 1
        }
    }

    private fun getIdMap(): HashMap<String, Long> {
        val type = object : TypeToken<HashMap<String, Long>>() {}.type
        return gson.fromJson(idMapJson, type)
    }

    private fun setIdMap(idMap: Map<String, Long>) {
        idMapJson = gson.toJson(idMap)
    }

    private fun <E> getClassPackageAndName(clazz: Class<E>): String {
        val packageName = clazz.`package`?.name ?: ""
        val className = clazz.simpleName ?: ""
        return if (packageName.isNotEmpty()) {
            "$packageName.$className"
        } else {
            className
        }
    }
}