package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.R
import com.mobile.fodein.domain.interactor.GetImageListUseCase
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.model.ImageModel
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

class ImageListPresenter @Inject constructor(private val getImageListUseCase:
                                             GetImageListUseCase):
        BasePresenter(){

    init {
        this.iHearMessage = getImageListUseCase
    }

    fun setVariables(valueField: String){
        getImageListUseCase.nameField = "idForm"
        getImageListUseCase.valueField = valueField
    }

    fun getListImages(valueField: String){
        if (!existInCache<ImageModel>(valueField)){
            getImageListUseCase.execute(ListObserver())
        }
    }

    override fun destroy() {
        super.destroy()
        this.getImageListUseCase.dispose()
    }

    private fun showCollectionInView(objectsList:
                                     List<ImageModel>){
        this.view!!.renderList(objectsList)
    }

    inner class ListObserver: DisposableObserver<List<ImageModel>>(){
        override fun onNext(t: List<ImageModel>) {
            if (t.isNotEmpty()){
                val idForm =  t[0].idForm
                CachingLruRepository.instance.getLru()
                        .put(idForm, t)
            }
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
