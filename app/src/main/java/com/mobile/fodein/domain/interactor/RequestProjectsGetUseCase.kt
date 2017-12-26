package com.mobile.fodein.domain.interactor

import com.google.gson.Gson
import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.domain.DeliveryOfResource
import com.mobile.fodein.domain.RequestGetUseCase
import com.mobile.fodein.models.persistent.network.MessageOfService
import com.mobile.fodein.models.persistent.network.ServiceRemoteGet
import com.mobile.fodein.presentation.model.ProjectModel
import com.mobile.fodein.tools.Constants
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject


class RequestProjectsGetUseCase @Inject constructor(serviceRemoteGet:
                                                    ServiceRemoteGet):
        RequestGetUseCase(serviceRemoteGet){
    //private var list: ArrayList<ProjectModel>? = null

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
        //list = ArrayList()

        (0 until jsonArray.length()).forEach { i ->
            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
            val project = ProjectModel()
            if (jsonObject.has("id")){
                project.idNet = jsonObject.getString("id")?: ""
            }
            if (jsonObject.has("type")){
                project.type = jsonObject.getInt("type")
            }
            if (jsonObject.has("code")){
                project.code = jsonObject.getString("code")?: ""
            }
            if (jsonObject.has("name")){
                project.name = jsonObject.getString("name")?: ""
            }
            if (jsonObject.has("latitude")){
                project.latitude = jsonObject.getDouble("latitude")
            }
            if (jsonObject.has("longitude")){
                project.longitude = jsonObject.getDouble("longitude")
            }
            if (jsonObject.has("finance")){
                project.finance = jsonObject.getDouble("finance")
            }
            if (jsonObject.has("counterpart")){
                project.counterpart = jsonObject.getDouble("counterpart")
            }
            if (jsonObject.has("notFinance")){
                project.notFinance = jsonObject.getDouble("notFinance")
            }
            if (jsonObject.has("other")){
                project.other = jsonObject.getDouble("other")
            }
            if (jsonObject.has("sum")){
                project.sum = jsonObject.getDouble("sum")
            }


            project.title = context.resources
                    .getString(R.string.hint_text_name) + ": " + project.name
            project.description = context.resources
                    .getString(R.string.lbl_latitude) + ": " +
                    project.latitude.toString() + "\n" + context.resources
                    .getString(R.string.lbl_longitude) + ": " +
                    project.longitude.toString()
            //list!!.add(project)
            if (jsonObject.has("unit_id")){
                val idUnit = jsonObject.getString("unit_id")?: ""

                DeliveryOfResource.setProjectToUnit(idUnit, project)
            }

        }

//        CachingLruRepository.instance.getLru()
//                .put(Constants.CACHE_LIST_PROJECT_MODEL, list!!.toList())

        this.messageEnd = Constants.END_TASK
        this.observableEndTask.onNext(this.messageEnd)
    }

    override fun sendMessageError(message: String) {
        this.messageError =  message
        this.observableMessage.onNext(this.messageError)
    }

}