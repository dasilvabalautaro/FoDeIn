package com.mobile.fodein.domain.interactor

import com.mobile.fodein.domain.UseCase
import com.mobile.fodein.domain.interfaces.IHearMessage
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.IProjectRepository
import com.mobile.fodein.models.exception.DatabaseOperationException
import com.mobile.fodein.presentation.model.ProjectModel
import io.reactivex.Observable
import javax.inject.Inject


class GetProjectListUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                postExecutionThread: IPostExecutionThread,
                                                private var projectRepository:
                                                IProjectRepository):
        UseCase<List<ProjectModel>>(threadExecutor, postExecutionThread),
        IHearMessage {
    override fun buildUseCaseObservable(): Observable<List<ProjectModel>> {
        return projectRepository.projectList()
    }
    override fun hearMessage(): Observable<String>{
        return this.projectRepository.userGetMessage()
    }

    override fun hearError(): Observable<DatabaseOperationException>{
        return this.projectRepository.userGetError()
    }
}