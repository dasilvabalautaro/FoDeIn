package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.domain.interactor.RequestRegisterFormPostUseCase
import com.mobile.fodein.presentation.interfaces.ILoadDataView
import com.mobile.fodein.tools.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class FormRegisterNetworkPresenter @Inject constructor(private val requestRegisterFormPostUseCase:
                                                       RequestRegisterFormPostUseCase){

    var view: ILoadDataView? = null
    private var disposable: CompositeDisposable = CompositeDisposable()

    init {
        val message = this.requestRegisterFormPostUseCase.observableMessage.map { s -> s }
        disposable.add(message.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    kotlin.run {
                        view!!.showError(s)
                    }
                })
        val user = this.requestRegisterFormPostUseCase.observableForm.map { u -> u }
        disposable.add(user.observeOn(AndroidSchedulers.mainThread())
                .subscribe { u ->
                    kotlin.run {
                        view!!.showMessage(u)
                    }
                })

    }

    fun setForm(data: MutableMap<String, Any>, token: String){
        requestRegisterFormPostUseCase.backPack = data
        requestRegisterFormPostUseCase.service = Constants.SERVICE_UPLOAD_FORMS
        requestRegisterFormPostUseCase.token = token
    }

    fun registerForm(){
        if (requestRegisterFormPostUseCase.createAgent() &&
                requestRegisterFormPostUseCase.createBody()){
            requestRegisterFormPostUseCase.getDataServer()
        }
    }

}