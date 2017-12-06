package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.R
import com.mobile.fodein.domain.interactor.GetFormSelectUseCase
import com.mobile.fodein.presentation.model.FormModel
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

class FormSelectPresenter @Inject constructor(private val getFormSelectUseCase:
                                              GetFormSelectUseCase):
        BasePresenter(){
    init {
        this.iHearMessage = getFormSelectUseCase
    }

    fun getSelectForm(){
        getFormSelectUseCase.execute(FormObserver())
    }

    fun setForm(idForm: String){
        getFormSelectUseCase.idForm = idForm
    }

    fun showFormDetailsInView(form: FormModel){
        this.view!!.renderObject(form)
    }
    override fun destroy() {
        super.destroy()
        this.getFormSelectUseCase.dispose()
    }

    inner class FormObserver : DisposableObserver<FormModel>() {
        override fun onNext(t: FormModel) {
            showFormDetailsInView(t)
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