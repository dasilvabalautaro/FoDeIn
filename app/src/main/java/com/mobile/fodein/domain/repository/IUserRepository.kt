package com.mobile.fodein.domain.repository

import com.mobile.fodein.domain.data.MapperUser
import com.mobile.fodein.models.data.User
import com.mobile.fodein.models.interfaces.IMessagePersistent
import io.reactivex.Observable

interface IUserRepository: IMessagePersistent {
    fun userList(): Observable<List<User>>
    fun userSave(user: MapperUser): Observable<MapperUser>
    fun userGetById(id: String): Observable<User>
}