package com.mobile.fodein.domain.interactor

import com.mobile.fodein.domain.UseCase
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.IProjectRepository
import io.reactivex.Observable
import javax.inject.Inject

class AddFormProjectListUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                    postExecutionThread: IPostExecutionThread,
                                                    private var projectRepository:
                                                    IProjectRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread){
    var idForm: String = ""
    var idProject: String = ""

    override fun buildUseCaseObservable(): Observable<Boolean> {
        return projectRepository.addForm(idForm, idProject)
    }

}