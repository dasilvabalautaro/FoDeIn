package com.mobile.fodein.models.persistent

import android.content.Context
import com.mobile.fodein.R
import com.mobile.fodein.models.interfaces.IPersistent
import io.realm.Realm
import io.realm.RealmConfiguration


class PersistentDatabase(private val context: Context) : IPersistent {
    private val schemaVersion: Long = 1
//    private var realm: Realm by Delegates.notNull()

    override fun create() {
        Realm.init(context)
        val configuration = RealmConfiguration.Builder()
                .name(context.resources.getString(R.string.database))
                .schemaVersion(schemaVersion)
                .deleteRealmIfMigrationNeeded()
                .build()

        Realm.setDefaultConfiguration(configuration)

    }

    override fun buildContent(){

    }

}

