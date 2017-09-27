package com.mobile.fodein.models.persistent.repository

import com.mobile.fodein.models.interfaces.IRepository
import com.mobile.fodein.models.interfaces.OnDatabaseCompleteListener
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.RealmResults

abstract class DatabaseRepository : IRepository {

    private val realm: Realm = Realm.getDefaultInstance()
    private val fieldId = "id"

    override fun <E : RealmObject> save(clazz: Class<E>,
                                          listener: OnDatabaseCompleteListener) {
        realm.executeTransactionAsync({ backgroundRealm ->
            backgroundRealm.createObject(clazz)

        }, {
            listener.onSaveSucceeded()
        }, { error ->
            listener.onSaveFailed(error.message!!)
        })
    }

    override fun <E : RealmObject> getAllData(clazz: Class<E>): RealmResults<E>? {
        return realm.where(clazz).findAll()
    }

    override fun <E : RealmObject> getDataByField(clazz: Class<E>,
                                                  fieldName: String,
                                                  value: String): RealmResults<E>? {
        return realm.where(clazz).equalTo(fieldName, value).findAll()
    }

    override fun <E : RealmObject> getDataById(clazz: Class<E>, value: String): E? {
        return realm.where(clazz).equalTo(fieldId, value).findFirst()
    }

    override fun <E> update(clazz: Class<E>,
                                          listener: OnDatabaseCompleteListener) {
        realm.executeTransactionAsync({ backgroundRealm ->
            backgroundRealm.copyToRealmOrUpdate(clazz as RealmModel)

        }, {
            listener.onUpdateCompleted()
        }, { error ->
            listener.onUpdateFailed(error.message!!)
        })
    }

    override fun <E : RealmObject> delete(clazz: Class<E>,
                                          fieldName: String,
                                          value: String,
                                          listener: OnDatabaseCompleteListener) {
        realm.executeTransactionAsync({ backgroundRealm ->
            backgroundRealm.where(clazz)
                    .equalTo(fieldName, value)
                    .findAll()?.deleteAllFromRealm()
        }, {
            listener.onDeleteCompleted()
        }, { error ->
            listener.onDeleteFailed(error.message!!)
        })
    }

    override fun destroy() {
        if (!realm.isClosed) realm.close()
    }
}


//val result: User? = realm.where(User::class.java)
//        .equalTo("user", user.user)
//        .equalTo("password", user.password)
//        .findFirst()

