package com.mobile.fodein.domain.interactor

import com.google.gson.Gson
import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.domain.DeliveryOfResource
import com.mobile.fodein.domain.RequestGetUseCase
import com.mobile.fodein.models.persistent.network.MessageOfService
import com.mobile.fodein.models.persistent.network.ServiceRemoteGet
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.model.ProjectModel
import com.mobile.fodein.presentation.model.UnityModel
import com.mobile.fodein.tools.Constants
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject


class RequestProjectsGetUseCase @Inject constructor(serviceRemoteGet:
                                                    ServiceRemoteGet):
        RequestGetUseCase(serviceRemoteGet){
    private var list: ArrayList<ProjectModel>? = null

    private var messageEnd: String = ""
    var observableEndTask: Subject<String> = PublishSubject.create()
    private val context = App.appComponent.context()

    init {
        observableEndTask
                .subscribe { messageEnd }
        observableMessage
                .subscribe { messageError }
    }



    override fun getJsonArray(messageOfService: MessageOfService) {
        val gson = Gson()
        if (messageOfService.success){
            val gsonResult = gson.toJson(messageOfService.result)

            if (!gsonResult.isEmpty()){
                val jsonArray =  JSONArray(gsonResult)
                getList(jsonArray)
            }

        }else{
            val gsonError = gson.toJson(messageOfService.error)
            this.messageError =  gsonError.toString()
            this.observableMessage.onNext(this.messageError)
        }

    }

    private fun getList(jsonArray: JSONArray){
        list = ArrayList()

        (0 until jsonArray.length()).forEach { i ->
            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
            val project = ProjectModel()
            project.idNet = jsonObject.getString("id")?: ""
            project.name = jsonObject.getString("name")?: ""
            project.phone = jsonObject.getString("phone")?: ""
            project.address = jsonObject.getString("address")?: ""
            project.title = context.resources
                    .getString(R.string.hint_text_name) + ": " + project.name
            project.description = context.resources
                    .getString(R.string.hint_text_address) + ": " +
                    project.address + "\n" + context.resources
                    .getString(R.string.hint_text_phone) + ": " + project.phone
            list!!.add(project)
            val idDistrict = jsonObject.getString("district_id")?: ""

            DeliveryOfResource.setUnitToDistrict(idDistrict, project)
        }

        CachingLruRepository.instance.getLru()
                .put(Constants.CACHE_LIST_UNITY_MODEL, list!!.toList())

        this.messageEnd = Constants.END_TASK
        this.observableEndTask.onNext(this.messageEnd)
    }

}