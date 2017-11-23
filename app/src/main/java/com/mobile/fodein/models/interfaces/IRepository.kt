package com.mobile.fodein.models.interfaces

import android.os.Parcel
import io.realm.RealmObject
import io.realm.RealmResults


interface IRepository {
    fun <E : RealmObject> getAllData(clazz: Class<E>): RealmResults<E>?
    fun <E : RealmObject> getDataById(clazz: Class<E>, value: String): E?
    fun <E : RealmObject> delete(clazz: Class<E>, fieldName: String, value: String,
                                 listener: OnDatabaseCompleteListener)
    fun <E : RealmObject> save(clazz: Class<E>, parcel: Parcel,
                               listener: OnDatabaseCompleteListener): E?
    fun <E> update(clazz: Class<E>, listener: OnDatabaseCompleteListener)
    fun <E : RealmObject> getDataByField(clazz: Class<E>,
                                         fieldName: String,
                                         value: String): RealmResults<E>?
    fun <E : RealmObject> addObjectList(clazz: Class<E>,
                                        value: String, newObject: Any,
                                        listener: OnDatabaseCompleteListener)
    fun saveFormInList(idProject: String, idForm: String): Boolean
    fun <E : RealmObject> updateUpload(id: String, clazz: Class<E>,
                         listener: OnDatabaseCompleteListener): Boolean
}