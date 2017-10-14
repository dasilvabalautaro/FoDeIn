package com.mobile.fodein.domain.repository

import com.mobile.fodein.domain.data.MapperUnity
import com.mobile.fodein.models.interfaces.IMessagePersistent
import com.mobile.fodein.presentation.model.UnityModel
import io.reactivex.Observable

interface IUnityRepository: IMessagePersistent {
    fun unityList(): Observable<List<UnityModel>>
    fun unitySave(unity: MapperUnity): Observable<UnityModel>
    fun unityGetById(id: String): Observable<UnityModel>
    fun unityGetByField(value: String, nameField: String): Observable<List<UnityModel>>

}