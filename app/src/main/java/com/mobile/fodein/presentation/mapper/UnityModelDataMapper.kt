package com.mobile.fodein.presentation.mapper

import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.models.data.Unity
import com.mobile.fodein.presentation.model.ProjectModel
import com.mobile.fodein.presentation.model.UnityModel


class UnityModelDataMapper {
    private val context = App.appComponent.context()
    private val projectModelDataMapper:
            ProjectModelDataMapper = ProjectModelDataMapper()
    private val districtModelDataMapper:
            DistrictModelDataMapper = DistrictModelDataMapper()

    fun transform(unity: Unity?): UnityModel {
        if (unity == null)
            throw IllegalArgumentException(context.getString(R.string.value_null))
        val unityModel = UnityModel()
        unityModel.id = unity.id
        unityModel.address = unity.address
        unityModel.district = districtModelDataMapper.transform(unity.district)
        val projectModelCollection: Collection<ProjectModel> = this
                .projectModelDataMapper
                .transform(unity.projects)
        unityModel.list = projectModelCollection as List<ProjectModel>
        unityModel.name = unity.name
        unityModel.phone = unity.phone
        return unityModel
    }

    fun transform(unityCollection: Collection<Unity>?): Collection<UnityModel>{
        val unityModelCollection: MutableCollection<UnityModel> = ArrayList()

        if (unityCollection != null && !unityCollection.isEmpty())
            unityCollection.mapTo(unityModelCollection) { transform(it) }
        return unityModelCollection
    }
}