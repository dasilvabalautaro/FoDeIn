package com.mobile.fodein.domain.interactor

import com.google.gson.Gson
import com.mobile.fodein.domain.RequestPostUseCase
import com.mobile.fodein.models.persistent.network.MessageOfService
import com.mobile.fodein.models.persistent.network.ServiceRemotePost
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class RequestRegisterFormPostUseCase @Inject constructor(serviceRemotePost:
                                                         ServiceRemotePost):
        RequestPostUseCase(serviceRemotePost){
    private var formRegister: String = ""
    var observableForm: Subject<String> = PublishSubject.create()

    init {
        observableForm
                .subscribe { formRegister }
        observableMessage
                .subscribe { messageError }
    }


    override fun getJsonArray(messageOfService: MessageOfService) {
        val gson = Gson()
        if (messageOfService.success){
            val gsonResult = gson.toJson(messageOfService.result)

            if (!gsonResult.isEmpty()){
                this.formRegister = gsonResult.toString()
                this.observableForm.onNext(this.formRegister)
            }

        }else{
            val gsonError = gson.toJson(messageOfService.error)
            this.messageError =  gsonError.toString()
            this.observableMessage.onNext(this.messageError)
        }
    }

}