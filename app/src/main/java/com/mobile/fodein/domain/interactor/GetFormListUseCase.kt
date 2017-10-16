package com.mobile.fodein.domain.interactor

import com.mobile.fodein.domain.UseCase
import com.mobile.fodein.domain.interfaces.IHearMessage
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.IFormRepository
import com.mobile.fodein.models.exception.DatabaseOperationException
import com.mobile.fodein.presentation.model.FormModel
import io.reactivex.Observable
import javax.inject.Inject


class GetFormListUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                             postExecutionThread: IPostExecutionThread,
                                             private var formRepository:
                                             IFormRepository):
        UseCase<List<FormModel>>(threadExecutor, postExecutionThread),
        IHearMessage {
    override fun buildUseCaseObservable(): Observable<List<FormModel>> {
        return formRepository.formList()
    }
    override fun hearMessage(): Observable<String>{
        return this.formRepository.userGetMessage()
    }

    override fun hearError(): Observable<DatabaseOperationException>{
        return this.formRepository.userGetError()
    }
}