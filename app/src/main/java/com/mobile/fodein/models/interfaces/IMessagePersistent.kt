package com.mobile.fodein.models.interfaces

import com.mobile.fodein.models.exception.DatabaseOperationException
import io.reactivex.Observable

interface IMessagePersistent {
    fun userGetMessage(): Observable<String>
    fun userGetError(): Observable<DatabaseOperationException>
}