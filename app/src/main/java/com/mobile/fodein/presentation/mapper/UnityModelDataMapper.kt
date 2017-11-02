package com.mobile.fodein.presentation.mapper

import android.content.Context
import com.mobile.fodein.R
import com.mobile.fodein.models.data.Unity
import com.mobile.fodein.presentation.model.ProjectModel
import com.mobile.fodein.presentation.model.UnityModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnityModelDataMapper @Inject constructor (val context: Context,
                                                val projectModelDataMapper:
                                                ProjectModelDataMapper) {

    fun transform(unity: Unity?): UnityModel {
        if (unity == null)
            throw IllegalArgumentException(context.getString(R.string.value_null))
        val unityModel = UnityModel()
        unityModel.id = unity.id
        unityModel.address = unity.address
        val projectModelCollection: Collection<ProjectModel> = this
                .projectModelDataMapper
                .transform(unity.projects)
        unityModel.list = projectModelCollection as ArrayList<ProjectModel>
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