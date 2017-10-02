package com.mobile.fodein.models.persistent.repository

import android.os.Parcel
import com.mobile.fodein.models.interfaces.IDataParcelable
import com.mobile.fodein.models.interfaces.IRepository
import com.mobile.fodein.models.interfaces.OnDatabaseCompleteListener
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.RealmResults
import java.util.*

abstract class DatabaseRepository : IRepository {

    private val fieldId = "id"

    override fun <E : RealmObject> save(clazz: Class<E>,
                                        parcel: Parcel,
                                          listener: OnDatabaseCompleteListener): E? {
        val realm: Realm = Realm.getDefaultInstance()

        var e: E? = null
        try {
            realm.executeTransaction({
                e = it.createObject(clazz, UUID.randomUUID().toString())
                if (e is IDataParcelable){
                    (e as IDataParcelable).readFromParcel(parcel)
                }

                listener.onSaveSucceeded()

            })
        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
        }
        return e

    }

    override fun <E : RealmObject> getAllData(clazz: Class<E>): RealmResults<E>? {
        val realm: Realm = Realm.getDefaultInstance()
        return realm.where(clazz).findAll()
    }

    override fun <E : RealmObject> getDataByField(clazz: Class<E>,
                                                  fieldName: String,
                                                  value: String): RealmResults<E>? {
        val realm: Realm = Realm.getDefaultInstance()
        return realm.where(clazz).equalTo(fieldName, value).findAll()
    }

    override fun <E : RealmObject> getDataById(clazz: Class<E>, value: String): E? {
        val realm: Realm = Realm.getDefaultInstance()
        return realm.where(clazz).equalTo(fieldId, value).findFirst()
    }

    override fun <E> update(clazz: Class<E>,
                                          listener: OnDatabaseCompleteListener) {
        val realm: Realm = Realm.getDefaultInstance()
        try {
            realm.executeTransaction({
                it.copyToRealmOrUpdate(clazz as RealmModel)
                listener.onSaveSucceeded()

            })
        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
        }finally {
            realm.close()
        }

    }

    override fun <E : RealmObject> delete(clazz: Class<E>,
                                          fieldName: String,
                                          value: String,
                                          listener: OnDatabaseCompleteListener) {
        val realm: Realm = Realm.getDefaultInstance()
        try {
            realm.executeTransaction({
                it.where(clazz)
                        .equalTo(fieldName, value)
                        .findAll()?.deleteAllFromRealm()
                listener.onSaveSucceeded()

            })
        }catch (e: Throwable){
            listener.onSaveFailed(e.message!!)
        }finally {
            realm.close()
        }

    }


}



//val result: User? = realm.where(User::class.java)
//        .equalTo("user", user.user)
//        .equalTo("password", user.password)
//        .findFirst()

