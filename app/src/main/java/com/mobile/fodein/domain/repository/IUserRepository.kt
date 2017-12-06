package com.mobile.fodein.domain.repository

import com.mobile.fodein.domain.data.MapperUser
import com.mobile.fodein.models.interfaces.IMessagePersistent
import com.mobile.fodein.presentation.model.UserModel
import io.reactivex.Observable

interface IUserRepository: IMessagePersistent {
    fun userList(): Observable<List<UserModel>>
    fun userSave(user: MapperUser): Observable<UserModel>
    fun userGetById(id: String): Observable<UserModel>
    fun userGetByField(value: String, nameField: String): Observable<List<UserModel>>
}