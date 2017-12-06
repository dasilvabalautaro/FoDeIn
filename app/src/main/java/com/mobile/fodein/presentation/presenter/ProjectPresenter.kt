package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.R
import com.mobile.fodein.domain.interactor.GetProjectListUseCase
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.model.ProjectModel
import com.mobile.fodein.tools.Constants
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class ProjectPresenter @Inject constructor(private val getProjectListUseCase:
                                           GetProjectListUseCase):
        BasePresenter(){
    init {
        this.iHearMessage = getProjectListUseCase
    }

    fun getListProject(clear: Boolean = false){

        if (!existInCache<ProjectModel>(Constants.CACHE_LIST_PROJECT_MODEL) ||
                clear){
            getProjectListUseCase.execute(ListObserver())
        }


    }
    private fun showCollectionInView(objectsList:
                                     List<ProjectModel>){
        this.view!!.renderList(objectsList)
    }

    override fun destroy() {
        super.destroy()
        this.getProjectListUseCase.dispose()
    }
    inner class ListObserver: DisposableObserver<List<ProjectModel>>(){
        override fun onNext(t: List<ProjectModel>) {
            CachingLruRepository.instance.getLru()
                    .put(Constants.CACHE_LIST_PROJECT_MODEL, t)
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