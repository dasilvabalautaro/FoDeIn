package com.mobile.fodein.domain

import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.model.DistrictModel
import com.mobile.fodein.presentation.model.UnityModel
import com.mobile.fodein.tools.Constants


object DeliveryOfResource {
    var token = ""

    fun setUnitToDistrict(id: String, unity: UnityModel){

        try {
            val list= CachingLruRepository
                    .instance
                    .getLru()
                    .get(Constants.CACHE_LIST_DISTRICT_MODEL)
            if (list != null && list is ArrayList<*>){
                list.indices
                    .map { list[it] as DistrictModel }
                    .filter { it.id == id }
                    .forEach { it.list.add(unity) }
            }

        }catch (te: TypeCastException){
            println(te.message)
        }

    }
}