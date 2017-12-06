package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.R
import com.mobile.fodein.domain.interactor.GetDistrictListUseCase
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.model.DistrictModel
import com.mobile.fodein.tools.Constants
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class DistrictPresenter @Inject constructor(private val getDistrictListUseCase:
                                            GetDistrictListUseCase):
        BasePresenter(){

    init {
        this.iHearMessage = getDistrictListUseCase
    }

    fun getListDistrict(){
        if (!existInCache<DistrictModel>(Constants.CACHE_LIST_DISTRICT_MODEL)){
            getDistrictListUseCase.execute(ListObserver())
        }

    }

    private fun showCollectionInView(objectsList:
                                     List<DistrictModel>){
        this.view!!.renderList(objectsList)
    }

    override fun destroy() {
        super.destroy()
        this.getDistrictListUseCase.dispose()
    }

    inner class ListObserver: DisposableObserver<List<DistrictModel>>(){
        override fun onNext(t: List<DistrictModel>) {
            CachingLruRepository.instance.getLru()
                    .put(Constants.CACHE_LIST_DISTRICT_MODEL, t)
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