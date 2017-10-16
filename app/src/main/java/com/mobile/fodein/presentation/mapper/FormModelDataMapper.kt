package com.mobile.fodein.presentation.mapper

import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.models.data.Form
import com.mobile.fodein.presentation.model.FormModel


class FormModelDataMapper {
    private val context = App.appComponent.context()
    private val projectModelDataMapper:
            ProjectModelDataMapper = ProjectModelDataMapper()
    private val userModelDataMapper: UserModelDataMapper = UserModelDataMapper()

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
        formModel.project = projectModelDataMapper.transform(form.project)
        formModel.user = userModelDataMapper.transform(form.user)
        return formModel
    }

    fun transform(formCollection: Collection<Form>?): Collection<FormModel>{
        val formModelCollection: MutableCollection<FormModel> = ArrayList()

        if (formCollection != null && !formCollection.isEmpty())
            formCollection.mapTo(formModelCollection) { transform(it) }
        return formModelCollection
    }
}