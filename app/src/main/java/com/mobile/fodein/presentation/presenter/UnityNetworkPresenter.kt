package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.R
import com.mobile.fodein.domain.DeliveryOfResource
import com.mobile.fodein.domain.interactor.AddUnitsDistrictListUseCase
import com.mobile.fodein.domain.interactor.RequestUnitsGetUseCase
import com.mobile.fodein.domain.interactor.UpdateUnityListUseCase
import com.mobile.fodein.presentation.model.UnityModel
import com.mobile.fodein.tools.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class UnityNetworkPresenter @Inject constructor(private val requestUnitsGetUseCase:
                                                RequestUnitsGetUseCase,
                                                private val updateUnityListUseCase:
                                                UpdateUnityListUseCase,
                                                private val addUnitsDistrictListUseCase:
                                                AddUnitsDistrictListUseCase):
        BasePresenter(){
    private var flag = 0
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
                        updateUnityListUseCase.execute(UpdateObserver())
                        view!!.showRetry()
                        view!!.showMessage(m)
                    }
                })

    }

    fun setVariables(token: String){
        requestUnitsGetUseCase.token = token
        requestUnitsGetUseCase.service = Constants.SERVICE_GET_UNITS
    }

    fun getList(){
        if (!existInCache<UnityModel>(Constants.CACHE_LIST_UNITY_MODEL,
                false)){
            if (requestUnitsGetUseCase.createAgent()){
                requestUnitsGetUseCase.getDataServer()
            }
        }

    }
    private fun updateObjectDatabase(result: Boolean){

        when(flag){
            0->{
                if (result){
                    addUnitsDistrictListUseCase.execute(UpdateObserver())
                    flag = 1
                }
            }
            1->{
                if (result){
                    DeliveryOfResource.updateDistrict = true
                    println(context.resources.getString(R.string.task_complete))
                }
            }
        }

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