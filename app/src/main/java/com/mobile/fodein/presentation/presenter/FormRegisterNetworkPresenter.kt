package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.domain.interactor.RequestRegisterFormPostUseCase
import com.mobile.fodein.domain.interactor.UpdateFormUploadUseCase
import com.mobile.fodein.presentation.interfaces.ILoadDataView
import com.mobile.fodein.tools.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

class FormRegisterNetworkPresenter @Inject constructor(private val requestRegisterFormPostUseCase:
                                                       RequestRegisterFormPostUseCase,
                                                       private val updateFormUploadUseCase:
                                                       UpdateFormUploadUseCase){

    var view: ILoadDataView? = null
    var idForm = ""
    val context = App.appComponent.context()

    private var disposable: CompositeDisposable = CompositeDisposable()

    init {
        val message = this.requestRegisterFormPostUseCase
                .observableMessage.map { s -> s }
        disposable.add(message.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    kotlin.run {
                        view!!.showError(s)
                    }
                })
        val user = this.requestRegisterFormPostUseCase.observableForm.map { u -> u }
        disposable.add(user.observeOn(AndroidSchedulers.mainThread())
                .subscribe { u ->
                    kotlin.run {
                        updateFormUploadUseCase.idForm = this.idForm
                        updateFormUploadUseCase.execute(AddObserver())
                        view!!.showMessage(u)
                    }
                })
    }

    fun setForm(data: MutableMap<String, Any>, token: String){
        this.idForm =  data[Constants.FORM_ID].toString()
        requestRegisterFormPostUseCase.backPack = data
        requestRegisterFormPostUseCase.service = Constants.SERVICE_UPLOAD_FORMS
        requestRegisterFormPostUseCase.token = token
    }

    fun registerForm(){
        if (requestRegisterFormPostUseCase.createAgent() &&
                requestRegisterFormPostUseCase.createBody()){
            requestRegisterFormPostUseCase.getDataServer()
        }
    }

    private fun addObjectDatabase(result: Boolean){
        view!!.showMessage(context.resources.getString(R.string.lbl_upload_form) +
                " " + result.toString())
    }


    inner class AddObserver: DisposableObserver<Boolean>(){
        override fun onNext(r: Boolean) {
            addObjectDatabase(r)
        }

        override fun onComplete() {
            view!!.showMessage(context.resources.getString(R.string.task_complete))
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                view!!.showError(e.message!!)
            }
        }
    }

}