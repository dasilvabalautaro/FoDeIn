package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.R
import com.mobile.fodein.domain.interactor.GetUnityListUseCase
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.model.UnityModel
import com.mobile.fodein.tools.Constants
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class UnityPresenter @Inject constructor(private val getUnityListUseCase:
                                         GetUnityListUseCase):
        BasePresenter(){

    init {
        this.iHearMessage = getUnityListUseCase
    }

    fun getListUnity(){
        if (!existInCache<UnityModel>(Constants.CACHE_LIST_UNITY_MODEL)){
            getUnityListUseCase.execute(ListObserver())
        }
    }

    private fun showCollectionInView(objectsList:
                                     List<UnityModel>){
        this.view!!.renderList(objectsList)
    }

    override fun destroy() {
        super.destroy()
        this.getUnityListUseCase.dispose()
    }

    inner class ListObserver: DisposableObserver<List<UnityModel>>(){
        override fun onNext(t: List<UnityModel>) {
            CachingLruRepository.instance.getLru()
                    .put(Constants.CACHE_LIST_UNITY_MODEL, t)
            showCollectionInView(t)
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