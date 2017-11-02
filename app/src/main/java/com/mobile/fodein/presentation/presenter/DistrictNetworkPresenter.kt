package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.domain.interactor.RequestDistrictGetUseCase
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.interfaces.ILoadDataView
import com.mobile.fodein.tools.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class DistrictNetworkPresenter @Inject constructor(private val requestDistrictGetUseCase:
                                                   RequestDistrictGetUseCase){

    var view: ILoadDataView? = null
    private var disposable: CompositeDisposable = CompositeDisposable()

    init {
        val message = this.requestDistrictGetUseCase.observableMessage.map { s -> s }
        disposable.add(message.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    kotlin.run {
                        view!!.showError(s)
                    }
                })
        val list = this.requestDistrictGetUseCase.observableList.map { l -> l }
        disposable.add(list.observeOn(AndroidSchedulers.mainThread())
                .subscribe { l ->
                    kotlin.run {
                        CachingLruRepository.instance.getLru()
                                .put(Constants.CACHE_LIST_DISTRICT_MODEL, l.toList())
                        view!!.renderList(l.toList())
                    }
                })

    }

    fun setVariables(token: String){
        requestDistrictGetUseCase.token = token
        requestDistrictGetUseCase.service = Constants.SERVICE_GET_DISTRICT
    }

    fun getList(){
        if (requestDistrictGetUseCase.createAgent()){
            requestDistrictGetUseCase.getDataServer()
        }
    }

}