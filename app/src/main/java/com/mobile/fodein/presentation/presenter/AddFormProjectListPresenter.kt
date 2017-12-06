package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.R
import com.mobile.fodein.domain.interactor.AddFormProjectListUseCase
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class AddFormProjectListPresenter @Inject constructor(private  val addFormProjectListUseCase:
                                                      AddFormProjectListUseCase):
        BasePresenter(){

    fun setVariables(idForm: String, idProject: String){
        addFormProjectListUseCase.idForm = idForm
        addFormProjectListUseCase.idProject = idProject
    }

    fun addFormListProject(){
        addFormProjectListUseCase.execute(UpdateObserver())
    }

    private fun updateObjectDatabase(result: Boolean){
        view!!.showRetry()
        showMessage(context.resources.getString(R.string.add_pattern) +
                " " + result.toString())
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