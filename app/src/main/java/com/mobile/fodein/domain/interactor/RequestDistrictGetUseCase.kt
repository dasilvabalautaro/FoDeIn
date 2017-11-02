package com.mobile.fodein.domain.interactor

import com.google.gson.Gson
import com.mobile.fodein.domain.RequestGetUseCase
import com.mobile.fodein.models.persistent.network.MessageOfService
import com.mobile.fodein.models.persistent.network.ServiceRemoteGet
import com.mobile.fodein.presentation.model.DistrictModel
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject


class RequestDistrictGetUseCase @Inject constructor(serviceRemoteGet:
                                                    ServiceRemoteGet):
        RequestGetUseCase(serviceRemoteGet){
    private var list: ArrayList<DistrictModel>? = null
    var observableList: Subject<ArrayList<DistrictModel>> = PublishSubject.create()

    init {
        observableList
                .subscribe { list }
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
            val district = DistrictModel()
            district.id = jsonObject.getString("id")?: ""
            district.name = jsonObject.getString("name")?: ""
            list!!.add(district)
        }
        this.observableList.onNext(this.list!!)
    }
}