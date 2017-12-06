package com.mobile.fodein.domain.interactor

import com.mobile.fodein.domain.UseCase
import com.mobile.fodein.domain.interfaces.IHearMessage
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.IUnityRepository
import com.mobile.fodein.models.exception.DatabaseOperationException
import com.mobile.fodein.presentation.model.UnityModel
import io.reactivex.Observable
import javax.inject.Inject


class GetUnityListUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                              postExecutionThread: IPostExecutionThread,
                                              private var unityRepository:
                                              IUnityRepository):
        UseCase<List<UnityModel>>(threadExecutor, postExecutionThread),
        IHearMessage {
    override fun buildUseCaseObservable(): Observable<List<UnityModel>> {
        return unityRepository.unityList()
    }

    override fun hearMessage(): Observable<String>{
        return this.unityRepository.userGetMessage()
    }

    override fun hearError(): Observable<DatabaseOperationException>{
        return this.unityRepository.userGetError()
    }
}