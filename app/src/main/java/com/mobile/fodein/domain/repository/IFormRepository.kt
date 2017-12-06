package com.mobile.fodein.domain.repository

import com.mobile.fodein.domain.data.MapperForm
import com.mobile.fodein.models.interfaces.IMessagePersistent
import com.mobile.fodein.presentation.model.FormModel
import io.reactivex.Observable


interface IFormRepository: IMessagePersistent {
    fun formList(): Observable<List<FormModel>>
    fun formSave(form: MapperForm): Observable<FormModel>
    fun formGetById(id: String): Observable<FormModel>
    fun formGetByField(value: String, nameField: String): Observable<List<FormModel>>
    fun formUpdateUpload(value: String): Observable<Boolean>
    fun formGetByFieldBoolean(value: Boolean, nameField: String):
            Observable<List<FormModel>>
}