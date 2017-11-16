package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.R
import com.mobile.fodein.domain.data.MapperImage
import com.mobile.fodein.domain.interactor.AddImageListUseCase
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

class AddImageListPresenter @Inject constructor(private val addImageListUseCase:
                                                AddImageListUseCase):
        BasePresenter(){

    fun setVariables(list: List<MapperImage>){
        addImageListUseCase.list = list
    }
    fun addImages(){
        addImageListUseCase.execute(AddObserver())
    }

    private fun addObjectDatabase(result: Boolean){
        //view!!.showRetry()
        showMessage(context.resources.getString(R.string.add_pattern) +
                " " + result.toString())
    }

    inner class AddObserver: DisposableObserver<Boolean>(){
        override fun onNext(r: Boolean) {
            addObjectDatabase(r)
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