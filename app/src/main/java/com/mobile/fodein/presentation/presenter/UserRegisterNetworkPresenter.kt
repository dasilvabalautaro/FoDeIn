package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.domain.interactor.RequestRegisterPostUseCase
import com.mobile.fodein.presentation.interfaces.ILoadDataView
import com.mobile.fodein.tools.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class UserRegisterNetworkPresenter @Inject constructor(private val
                                                       requestRegisterPostUseCase:
                                                       RequestRegisterPostUseCase){
    var view: ILoadDataView? = null
    private var disposable: CompositeDisposable = CompositeDisposable()

    init {
        val message = this.requestRegisterPostUseCase.observableMessage.map { s -> s }
        disposable.add(message.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    kotlin.run {
                        view!!.showError(s)
                    }
                })
        val user = this.requestRegisterPostUseCase.observableUser.map { u -> u }
        disposable.add(user.observeOn(AndroidSchedulers.mainThread())
                .subscribe { u ->
                    kotlin.run {
                        view!!.showMessage(u)
                    }
                })

    }

    fun setUser(data: MutableMap<String, Any>){
        requestRegisterPostUseCase.backPack = data
        requestRegisterPostUseCase.service = Constants.SERVICE_REGISTER_USER
    }

    fun registerUser(){
        if (requestRegisterPostUseCase.createAgent() &&
                requestRegisterPostUseCase.createBody()){
            requestRegisterPostUseCase.getDataServer()
        }
    }
}