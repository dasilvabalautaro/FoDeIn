package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.domain.interactor.RequestLoginGetUseCase
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.interfaces.ILoadDataView
import com.mobile.fodein.tools.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class UserLoginNetworkPresenter @Inject constructor(private val requestLoginGetUseCase:
                                                    RequestLoginGetUseCase){

    var view: ILoadDataView? = null
    private var disposable: CompositeDisposable = CompositeDisposable()

    init {
        val message = this.requestLoginGetUseCase.observableMessage.map { s -> s }
        disposable.add(message.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    kotlin.run {
                        view!!.showError(s)
                    }
                })
        val user = this.requestLoginGetUseCase.observableUser.map { u -> u }
        disposable.add(user.observeOn(AndroidSchedulers.mainThread())
                .subscribe { u ->
                    kotlin.run {
                        CachingLruRepository.instance.getLru()
                                .put(Constants.CACHE_USER_MODEL, u)
                        view!!.renderObject(u)
                    }
                })

    }

    fun setUser(data: MutableMap<String, Any>){
        requestLoginGetUseCase.nameUser = data[Constants.USER_USER].toString()
        requestLoginGetUseCase.password = data[Constants.USER_PASSWORD].toString()
        requestLoginGetUseCase.service = Constants.SERVICE_LOGIN
    }

    fun verifyLogin(){
        if (requestLoginGetUseCase.createAgent()){
            requestLoginGetUseCase.getDataServer()
        }
    }

}