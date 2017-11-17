package com.mobile.fodein.domain.interactor

import com.google.gson.Gson
import com.mobile.fodein.domain.RequestGetUseCase
import com.mobile.fodein.models.persistent.network.MessageOfService
import com.mobile.fodein.models.persistent.network.ServiceRemoteGet
import com.mobile.fodein.presentation.model.UserModel
import com.mobile.fodein.tools.ConnectionNetwork
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
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
                user.id = jsonObject.getString("id")?: ""
                user.name = jsonObject.getString("name")?: ""
                user.user = jsonObject.getString("user")?: ""
                user.idCard = jsonObject.getString("idCard")?: ""
                user.email = jsonObject.getString("email")?: ""
                user.password = jsonObject.getString("password")?: ""
                user.phone = jsonObject.getString("phone")?: ""
                user.address = jsonObject.getString("address")?: ""
                user.description = jsonObject.getString("description")?: ""
                user.roll = jsonObject.getString("roll")?: ""
                user.token = jsonObject.getString("token")?: ""
                user.unit = jsonObject.getString("unit_id")?: ""
                user.image = jsonObject.getString("image")?: ""
                this.observableUser.onNext(this.user)
            }

        }else{
            val gsonError = gson.toJson(messageOfService.error)
            this.messageError =  gsonError.toString()
            this.observableMessage.onNext(this.messageError)
        }
    }


}