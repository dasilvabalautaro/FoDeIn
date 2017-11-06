package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.R
import com.mobile.fodein.domain.DeliveryOfResource
import com.mobile.fodein.domain.interactor.AddProjectsUnitsListUseCase
import com.mobile.fodein.domain.interactor.RequestProjectsGetUseCase
import com.mobile.fodein.domain.interactor.UpdateProjectListUseCase
import com.mobile.fodein.presentation.model.ProjectModel
import com.mobile.fodein.tools.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class ProjectNetworkPresenter @Inject constructor(private val requestProjectsGetUseCase:
                                                  RequestProjectsGetUseCase,
                                                  private val updateProjectListUseCase:
                                                  UpdateProjectListUseCase,
                                                  private val addProjectsUnitsListUseCase:
                                                  AddProjectsUnitsListUseCase):
        BasePresenter(){
    private var flag = 0
    init {
        val message = this.requestProjectsGetUseCase.observableMessage.map { s -> s }
        disposable.add(message.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    kotlin.run {
                        view!!.showError(s)
                    }
                })
        val messageEndTask = this.requestProjectsGetUseCase.observableEndTask.map { m -> m }
        disposable.add(messageEndTask.observeOn(AndroidSchedulers.mainThread())
                .subscribe { m ->
                    kotlin.run {
                        updateProjectListUseCase.execute(UpdateObserver())
                        view!!.showRetry()
                        view!!.showMessage(m)
                    }
                })

    }

    fun setVariables(token: String){
        requestProjectsGetUseCase.token = token
        requestProjectsGetUseCase.service = Constants.SERVICE_GET_PROJECTS
    }

    fun getList(){
        if (!existInCache<ProjectModel>(Constants.CACHE_LIST_PROJECT_MODEL,
                false)){
            if (requestProjectsGetUseCase.createAgent()){
                requestProjectsGetUseCase.getDataServer()
            }
        }
    }

    private fun updateObjectDatabase(result: Boolean){

        when(flag){
            0->{
                if (result){
                    addProjectsUnitsListUseCase.execute(UpdateObserver())
                    view!!.hideRetry()
                    flag = 1
                }
            }
            1->{
                if (result){
                    DeliveryOfResource.updateProjects = true
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