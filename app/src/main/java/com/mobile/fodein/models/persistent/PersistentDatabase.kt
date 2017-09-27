package com.mobile.fodein.models.persistent

import android.content.Context
import com.mobile.fodein.R
import com.mobile.fodein.models.data.User
import com.mobile.fodein.models.interfaces.IPersistent
import io.realm.Realm
import io.realm.RealmConfiguration


class PersistentDatabase(private val context: Context) : IPersistent {
    private val schemaVersion: Long = 1
    private var realm: Realm? = null

    override fun create() {
        Realm.init(context)
        val configuration = RealmConfiguration.Builder()
                .name(context.resources.getString(R.string.database))
                .schemaVersion(schemaVersion)
                .deleteRealmIfMigrationNeeded()
                .build()

        Realm.setDefaultConfiguration(configuration)
        realm = Realm.getInstance(configuration)
    }

    override fun buildContent(){
        val user: User? = realm!!.where(User::class.java).findFirst()
        if (user == null) realm!!.createObject(User::class.java)
    }

}