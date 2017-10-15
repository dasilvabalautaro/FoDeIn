package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.R
import com.mobile.fodein.domain.interactor.GetDistrictListUseCase
import com.mobile.fodein.presentation.model.DistrictModel
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class DistrictPresenter @Inject constructor(private val getDistrictListUseCase:
                                            GetDistrictListUseCase):
        BasePresenter(){

    init {
        this.iHearMessage = getDistrictListUseCase
    }

    fun getListDistrict(){
        getDistrictListUseCase.execute(ListObserver())
    }

    private fun showCollectionInView(usersModelCollection:
                                          Collection<DistrictModel>){
        this.view!!.renderList(usersModelCollection)
    }

    override fun destroy() {
        super.destroy()
        this.getDistrictListUseCase.dispose()
    }

    inner class ListObserver: DisposableObserver<List<DistrictModel>>(){
        override fun onNext(t: List<DistrictModel>) {
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