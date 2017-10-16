package com.mobile.fodein.presentation.mapper

import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.models.data.Project
import com.mobile.fodein.presentation.model.FormModel
import com.mobile.fodein.presentation.model.ProjectModel


class ProjectModelDataMapper {
    private val context = App.appComponent.context()
    private val unityModelDataMapper:
            UnityModelDataMapper = UnityModelDataMapper()
    private val formModelDataMapper:
            FormModelDataMapper = FormModelDataMapper()

    fun transform(project: Project?): ProjectModel {
        if (project == null)
            throw IllegalArgumentException(context.getString(R.string.value_null))
        val projectModel = ProjectModel()
        projectModel.id = project.id
        projectModel.code = project.code
        projectModel.counterpart = project.counterpart
        projectModel.finance = project.finance
        projectModel.latitude = project.latitude
        projectModel.longitude = project.longitude
        projectModel.name = project.name
        projectModel.notFinance = project.notFinance
        projectModel.other = project.other
        projectModel.sum = project.sum
        projectModel.type = project.type
        projectModel.unity = unityModelDataMapper.transform(project.unity)
        val formModelCollection: Collection<FormModel> = this
                .formModelDataMapper
                .transform(project.forms)
        projectModel.list = formModelCollection as List<FormModel>
        return projectModel
    }

    fun transform(projectCollection: Collection<Project>?): Collection<ProjectModel>{
        val projectModelCollection: MutableCollection<ProjectModel> = ArrayList()

        if (projectCollection != null && !projectCollection.isEmpty())
            projectCollection.mapTo(projectModelCollection) { transform(it) }
        return projectModelCollection
    }
}