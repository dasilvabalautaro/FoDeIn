package com.mobile.fodein.domain

import android.os.Build
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.model.DistrictModel
import com.mobile.fodein.presentation.model.ProjectModel
import com.mobile.fodein.presentation.model.UnityModel
import com.mobile.fodein.tools.Constants
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


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

    fun getDateTime(): String{
        var result = ""
        result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            val formatted = current.format(formatter)
            formatted
        } else {
            val c: Calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH)
            dateFormat.format(c.time)
        }

        return result
    }
}