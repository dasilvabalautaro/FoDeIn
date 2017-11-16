package com.mobile.fodein.domain.repository

import com.mobile.fodein.domain.data.MapperImage
import com.mobile.fodein.models.interfaces.IMessagePersistent
import com.mobile.fodein.presentation.model.ImageModel
import io.reactivex.Observable


interface IImageRepository: IMessagePersistent {
    fun imageSave(image: MapperImage): Observable<ImageModel>
    fun imageGetByField(value: String, nameField: String):
            Observable<List<ImageModel>>
    fun addListImage(list: List<MapperImage>): Observable<Boolean>
}