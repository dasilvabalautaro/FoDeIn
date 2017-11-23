package com.mobile.fodein.domain.interactor

import com.google.gson.Gson
import com.mobile.fodein.domain.RequestGetUseCase
import com.mobile.fodein.models.persistent.network.MessageOfService
import com.mobile.fodein.models.persistent.network.ServiceRemoteGet
import com.mobile.fodein.presentation.model.UserModel
import com.mobile.fodein.tools.ConnectionNetwork
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject


class RequestLoginGetUseCase @Inject constructor(serviceRemoteGet:
                                       ServiceRemoteGet,
                                                 connectionNetwork:
                                                 ConnectionNetwork):
        RequestGetUseCase(serviceRemoteGet, connectionNetwork){
    val user: UserModel = UserModel()
    var observableUser: Subject<UserModel> = PublishSubject.create()

    init {
        observableUser
                .subscribe { user }
        observableMessage
                .subscribe { messageError }
    }

    override fun getJsonArray(messageOfService: MessageOfService){
        val gson = Gson()
        if (messageOfService.success){
            val gsonResult = gson.toJson(messageOfService.result)

            if (!gsonResult.isEmpty()){
                val jsonObject = JSONObject(gsonResult)
                try {

                    user.id = jsonObject.getString("id")?: ""
                    user.name = jsonObject.getString("name")?: ""
                    user.user = jsonObject.getString("user")?: ""
                    user.password = jsonObject.getString("password")?: ""
                    user.unit = jsonObject.getString("unit_id")?: ""
                    user.token = jsonObject.getString("token")?: ""
                    if (jsonObject.has("idCard")){
                        user.idCard = jsonObject.getString("idCard")?: ""
                    }
                    if (jsonObject.has("email")){
                        user.email = jsonObject.getString("email")?: ""
                    }

                    if (jsonObject.has("phone")){
                        user.phone = jsonObject.getString("phone")?: ""
                    }
                    if (jsonObject.has("address")){
                        user.address = jsonObject.getString("address")?: ""
                    }
                    if (jsonObject.has("description")){
                        user.description = jsonObject.getString("description")?: ""
                    }
                    if (jsonObject.has("roll")){
                        user.roll = jsonObject.getString("roll")?: ""
                    }
                    if (jsonObject.has("image")){
                        user.image = jsonObject.getString("image")?: ""
                    }

                }catch (ne: JSONException){
                    println(ne.message)
                }
                this.observableUser.onNext(this.user)
            }

        }else{
            val gsonError = gson.toJson(messageOfService.error)
            this.messageError =  gsonError.toString()
            this.observableMessage.onNext(this.messageError)
        }
    }


}