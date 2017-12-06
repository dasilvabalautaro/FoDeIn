package com.mobile.fodein.presentation.mapper

import android.content.Context
import com.mobile.fodein.R
import com.mobile.fodein.models.data.Image
import com.mobile.fodein.presentation.model.ImageModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageModelDataMapper @Inject constructor(val context: Context) {
    fun transform(image: Image?): ImageModel {
        if (image == null)
            throw IllegalArgumentException(context.getString(R.string.value_null))
        val imageModel = ImageModel()
        imageModel.id = image.id
        imageModel.date = image.date
        imageModel.latitude = image.latitude
        imageModel.longitude = image.longitude
        imageModel.image = image.image
        imageModel.idForm = image.idForm
        return imageModel
    }

    fun transform(formCollection: Collection<Image>?): Collection<ImageModel>{
        val formModelCollection: MutableCollection<ImageModel> = ArrayList()

        if (formCollection != null && !formCollection.isEmpty())
            formCollection.mapTo(formModelCollection) { transform(it) }
        return formModelCollection
    }
}