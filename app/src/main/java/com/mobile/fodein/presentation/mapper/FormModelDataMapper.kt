package com.mobile.fodein.presentation.mapper

import android.content.Context
import com.mobile.fodein.R
import com.mobile.fodein.models.data.Form
import com.mobile.fodein.presentation.model.FormModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FormModelDataMapper @Inject constructor(val context: Context) {
    fun transform(form: Form?): FormModel {
        if (form == null)
            throw IllegalArgumentException(context.getString(R.string.value_null))
        val formModel = FormModel()
        formModel.id = form.id
        formModel.annotation = form.annotation
        formModel.annotationOne = form.annotationOne
        formModel.annotationTwo = form.annotationTwo
        formModel.date = form.date
        formModel.dateUpdate = form.dateUpdate
        formModel.latitude = form.latitude
        formModel.longitude = form.longitude
        return formModel
    }

    fun transform(formCollection: Collection<Form>?): Collection<FormModel>{
        val formModelCollection: MutableCollection<FormModel> = ArrayList()

        if (formCollection != null && !formCollection.isEmpty())
            formCollection.mapTo(formModelCollection) { transform(it) }
        return formModelCollection
    }
}