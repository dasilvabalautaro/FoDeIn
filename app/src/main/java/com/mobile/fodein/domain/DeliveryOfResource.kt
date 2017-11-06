package com.mobile.fodein.domain

import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.model.DistrictModel
import com.mobile.fodein.presentation.model.ProjectModel
import com.mobile.fodein.presentation.model.UnityModel
import com.mobile.fodein.tools.Constants


object DeliveryOfResource {
    var token = ""
    var updateDistrict = false
    var updateProjects = false
    var updateForms = false
    var userId = ""

    fun setUnitToDistrict(id: String, unity: UnityModel){

        try {
            val list= CachingLruRepository
                    .instance
                    .getLru()
                    .get(Constants.CACHE_LIST_DISTRICT_MODEL)
            if (list != null && list is ArrayList<*>){
                list.indices
                    .map { list[it] as DistrictModel }
                    .filter { it.idNet == id }
                    .forEach { it.list.add(unity) }

            }

        }catch (te: TypeCastException){
            println(te.message)
        }

    }

    fun setProjectToUnit(id: String, project: ProjectModel){

        try {
            val list= CachingLruRepository
                    .instance
                    .getLru()
                    .get(Constants.CACHE_LIST_UNITY_MODEL)
            if (list != null && list is ArrayList<*>){
                list.indices
                        .map { list[it] as UnityModel }
                        .filter { it.idNet == id }
                        .forEach { it.list.add(project) }

            }

        }catch (te: TypeCastException){
            println(te.message)
        }

    }
}