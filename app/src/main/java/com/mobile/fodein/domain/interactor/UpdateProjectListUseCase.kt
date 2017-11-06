package com.mobile.fodein.domain.interactor

import com.mobile.fodein.domain.UseCase
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.IProjectRepository
import io.reactivex.Observable
import javax.inject.Inject


class UpdateProjectListUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                   postExecutionThread: IPostExecutionThread,
                                                   private var projectRepository:
                                                   IProjectRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread){
    override fun buildUseCaseObservable(): Observable<Boolean> {
        return projectRepository.projectUpdate()
    }
}