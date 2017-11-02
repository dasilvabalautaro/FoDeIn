package com.mobile.fodein.domain.interactor

import com.google.gson.Gson
import com.mobile.fodein.domain.DeliveryOfResource
import com.mobile.fodein.domain.RequestGetUseCase
import com.mobile.fodein.models.persistent.network.MessageOfService
import com.mobile.fodein.models.persistent.network.ServiceRemoteGet
import com.mobile.fodein.presentation.model.UnityModel
import com.mobile.fodein.tools.Constants
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject


class RequestUnitsGetUseCase @Inject constructor(serviceRemoteGet:
                                                 ServiceRemoteGet):
        RequestGetUseCase(serviceRemoteGet){
    private var list: ArrayList<UnityModel>? = null
    private var messageEnd: String = ""
    var observableEndTask: Subject<String> = PublishSubject.create()

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
            val unity = UnityModel()
            unity.id = jsonObject.getString("id")?: ""
            unity.name = jsonObject.getString("name")?: ""
            unity.phone = jsonObject.getString("phone")?: ""
            unity.address = jsonObject.getString("address")?: ""
            val idDistrict = jsonObject.getString("district_id")?: ""
            list!!.add(unity)
            DeliveryOfResource.setUnitToDistrict(idDistrict, unity)
        }
        this.messageEnd = Constants.END_TASK
        this.observableEndTask.onNext(this.messageEnd)
    }
}