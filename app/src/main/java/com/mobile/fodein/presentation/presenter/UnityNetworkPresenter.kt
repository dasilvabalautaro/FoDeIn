package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.domain.interactor.RequestUnitsGetUseCase
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.interfaces.ILoadDataView
import com.mobile.fodein.tools.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class UnityNetworkPresenter @Inject constructor(private val requestUnitsGetUseCase:
                                                RequestUnitsGetUseCase){
    var view: ILoadDataView? = null
    private var disposable: CompositeDisposable = CompositeDisposable()

    init {
        val message = this.requestUnitsGetUseCase.observableMessage.map { s -> s }
        disposable.add(message.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    kotlin.run {
                        view!!.showError(s)
                    }
                })
        val messageEndTask = this.requestUnitsGetUseCase.observableEndTask.map { m -> m }
        disposable.add(messageEndTask.observeOn(AndroidSchedulers.mainThread())
                .subscribe { m ->
                    kotlin.run {
                        val list: List<*> = CachingLruRepository
                                .instance
                                .getLru()
                                .get(Constants.CACHE_LIST_DISTRICT_MODEL) as List<*>
                        view!!.renderList(list)
                        view!!.showMessage(m)
                    }
                })

    }

    fun setVariables(token: String){
        requestUnitsGetUseCase.token = token
        requestUnitsGetUseCase.service = Constants.SERVICE_GET_UNITS
    }

    fun getList(){
        if (requestUnitsGetUseCase.createAgent()){
            requestUnitsGetUseCase.getDataServer()
        }
    }
}