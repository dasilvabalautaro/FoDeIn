package com.mobile.fodein.domain.interactor

import com.google.gson.Gson
import com.mobile.fodein.domain.RequestPostUseCase
import com.mobile.fodein.models.persistent.network.MessageOfService
import com.mobile.fodein.models.persistent.network.ServiceRemotePost
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject


class RequestRegisterPostUseCase @Inject constructor(serviceRemotePost:
                                                     ServiceRemotePost):
                                    RequestPostUseCase(serviceRemotePost){

    private var userRegister: String = ""
    var observableUser: Subject<String> = PublishSubject.create()

    init {
        observableUser
                .subscribe { userRegister }
        observableMessage
                .subscribe { messageError }
    }

    override fun getJsonArray(messageOfService: MessageOfService){
        val gson = Gson()
        if (messageOfService.success){
            val gsonResult = gson.toJson(messageOfService.result)

            if (!gsonResult.isEmpty()){
                this.userRegister = gsonResult.toString()
                this.observableUser.onNext(this.userRegister)
            }

        }else{
            val gsonError = gson.toJson(messageOfService.error)
            this.messageError =  gsonError.toString()
            this.observableMessage.onNext(this.messageError)
        }
    }
    override fun sendMessageError(message: String) {
        this.messageError =  message
        this.observableMessage.onNext(this.messageError)
    }
}