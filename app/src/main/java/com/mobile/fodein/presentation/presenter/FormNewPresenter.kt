package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.R
import com.mobile.fodein.domain.interactor.GetFormNewUseCase
import com.mobile.fodein.presentation.model.FormModel
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

class FormNewPresenter @Inject constructor(private val getFormNewUseCase:
                                           GetFormNewUseCase):
        BasePresenter() {

    init {
        this.iHearMessage = getFormNewUseCase
    }

    fun registerForm(){
        getFormNewUseCase.execute(FormObserver())
    }

    fun setForm(data: MutableMap<String, Any>){
        getFormNewUseCase.setForm(data)
    }

    fun showFormDetailsInView(form: FormModel){
        this.view!!.renderObject(form)
    }

    override fun destroy() {
        super.destroy()
        this.getFormNewUseCase.dispose()
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