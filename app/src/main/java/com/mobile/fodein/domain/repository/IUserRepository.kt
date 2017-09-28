package com.mobile.fodein.domain.repository

import com.mobile.fodein.models.data.User
import io.reactivex.Observable

interface IUserRepository {
    fun userList(): Observable<List<User>>
    fun userSave(user: User): Observable<User>
    fun userGetById(id: String): Observable<User>
}