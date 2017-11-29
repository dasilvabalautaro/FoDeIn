package com.mobile.fodein.domain.interactor

import com.google.gson.Gson
import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.domain.DeliveryOfResource
import com.mobile.fodein.domain.RequestGetUseCase
import com.mobile.fodein.models.persistent.network.MessageOfService
import com.mobile.fodein.models.persistent.network.ServiceRemoteGet
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.model.UnityModel
import com.mobile.fodein.tools.ConnectionNetwork
import com.mobile.fodein.tools.Constants
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject


class RequestUnitsGetUseCase @Inject constructor(serviceRemoteGet:
                                                 ServiceRemoteGet,
                                                 connectionNetwork:
                                                 ConnectionNetwork):
        RequestGetUseCase(serviceRemoteGet, connectionNetwork){
    private var list: ArrayList<UnityModel>? = null

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
            val unity = UnityModel()
            if (jsonObject.has("id")){
                unity.idNet = jsonObject.getString("id")?: ""
            }
            if (jsonObject.has("name")){
                unity.name = jsonObject.getString("name")?: ""
            }
            if (jsonObject.has("phone")){
                unity.phone = jsonObject.getString("phone")?: ""
            }
            if (jsonObject.has("address")){
                unity.address = jsonObject.getString("address")?: ""
            }

            unity.title = context.resources
                    .getString(R.string.hint_text_name) + ": " + unity.name
            unity.description = context.resources
                    .getString(R.string.hint_text_address) + ": " +
                    unity.address + "\n" + context.resources
                    .getString(R.string.hint_text_phone) + ": " + unity.phone
            list!!.add(unity)
            if (jsonObject.has("district_id")){
                val idDistrict = jsonObject.getString("district_id")?: ""

                DeliveryOfResource.setUnitToDistrict(idDistrict, unity)
            }

        }

        CachingLruRepository.instance.getLru()
                .put(Constants.CACHE_LIST_UNITY_MODEL, list!!.toList())

        this.messageEnd = Constants.END_TASK
        this.observableEndTask.onNext(this.messageEnd)
    }
}