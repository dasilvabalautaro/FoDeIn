package com.mobile.fodein.presentation.mapper

import android.content.Context
import com.mobile.fodein.R
import com.mobile.fodein.models.data.District
import com.mobile.fodein.presentation.model.DistrictModel
import com.mobile.fodein.presentation.model.UnityModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DistrictModelDataMapper @Inject constructor(val context: Context,
                                                  private val unityModelDataMapper:
                                                  UnityModelDataMapper){

    fun transform(district: District?): DistrictModel {
        if (district == null)
            throw IllegalArgumentException(context.getString(R.string.value_null))
        val districtModel = DistrictModel()
        districtModel.id = district.id
        districtModel.name = district.name
        val unityModelCollection: Collection<UnityModel> = this
                .unityModelDataMapper
                .transform(district.unities)
        districtModel.list = unityModelCollection as ArrayList<UnityModel>
        return districtModel
    }

    fun transform(districtCollection: Collection<District>?): Collection<DistrictModel>{
        val districtModelCollection: MutableCollection<DistrictModel> = ArrayList()

        if (districtCollection != null && !districtCollection.isEmpty())
            districtCollection.mapTo(districtModelCollection) { transform(it) }
        return districtModelCollection
    }
}