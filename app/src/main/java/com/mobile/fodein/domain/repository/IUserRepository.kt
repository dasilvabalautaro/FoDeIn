package com.mobile.fodein.domain.repository

import com.mobile.fodein.models.data.User
import io.reactivex.Observable

interface IUserRepository {
    fun userList(): Observable<List<User>>
    fun user(id: String): Observable<User>
}