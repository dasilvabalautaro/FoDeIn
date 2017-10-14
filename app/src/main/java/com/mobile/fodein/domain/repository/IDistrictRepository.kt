package com.mobile.fodein.domain.repository

import com.mobile.fodein.domain.data.MapperDistrict
import com.mobile.fodein.models.interfaces.IMessagePersistent
import com.mobile.fodein.presentation.model.DistrictModel
import io.reactivex.Observable


interface IDistrictRepository: IMessagePersistent {
    fun districtList(): Observable<List<DistrictModel>>
    fun districtSave(district: MapperDistrict): Observable<DistrictModel>
    fun districtGetById(id: String): Observable<DistrictModel>
    fun districtGetByField(value: String, nameField: String): Observable<List<DistrictModel>>
}