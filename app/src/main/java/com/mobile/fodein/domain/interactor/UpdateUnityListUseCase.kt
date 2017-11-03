package com.mobile.fodein.domain.interactor

import com.mobile.fodein.domain.UseCase
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.IUnityRepository
import io.reactivex.Observable
import javax.inject.Inject


class UpdateUnityListUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                 postExecutionThread: IPostExecutionThread,
                                                 private var unityRepository:
                                                 IUnityRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread){
    override fun buildUseCaseObservable(): Observable<Boolean> {
        return unityRepository.unityUpdate()
    }
}