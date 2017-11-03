package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.R
import com.mobile.fodein.domain.interactor.RequestDistrictGetUseCase
import com.mobile.fodein.domain.interactor.UpdateDistrictListUseCase
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.model.DistrictModel
import com.mobile.fodein.tools.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class DistrictNetworkPresenter @Inject constructor(private val requestDistrictGetUseCase:
                                                   RequestDistrictGetUseCase,
                                                   private val updateDistrictListUseCase:
                                                   UpdateDistrictListUseCase):
        BasePresenter(){

    init {
        val message = this.requestDistrictGetUseCase.observableMessage.map { s -> s }
        disposable.add(message.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    kotlin.run {
                        showError(s)
                    }
                })
        val list = this.requestDistrictGetUseCase.observableList.map { l -> l }
        disposable.add(list.observeOn(AndroidSchedulers.mainThread())
                .subscribe { l ->
                    kotlin.run {
                        CachingLruRepository.instance.getLru()
                                .put(Constants.CACHE_LIST_DISTRICT_MODEL, l.toList())
                        updateDistrictListUseCase.execute(UpdateObserver())
                        view!!.renderList(l.toList())
                    }
                })

    }

    fun setVariables(token: String){
        requestDistrictGetUseCase.token = token
        requestDistrictGetUseCase.service = Constants.SERVICE_GET_DISTRICT
    }

    fun getList(){
        if (!existInCache<DistrictModel>(Constants.CACHE_LIST_DISTRICT_MODEL)){
            if (requestDistrictGetUseCase.createAgent()){
                requestDistrictGetUseCase.getDataServer()
            }
        }

    }

    private fun updateObjectDatabase(result: Boolean){
        if (result)
            println(context.resources.getString(R.string.task_complete))
    }

    inner class UpdateObserver: DisposableObserver<Boolean>(){
        override fun onNext(r: Boolean) {
            updateObjectDatabase(r)
        }

        override fun onComplete() {
            showMessage(context.resources.getString(R.string.task_complete))
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }
}