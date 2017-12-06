package com.mobile.fodein.domain.interfaces

import com.mobile.fodein.models.exception.DatabaseOperationException
import io.reactivex.Observable


interface IHearMessage {
    fun hearMessage(): Observable<String>
    fun hearError(): Observable<DatabaseOperationException>
}