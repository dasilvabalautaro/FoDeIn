package com.mobile.fodein.domain.interactor

import com.mobile.fodein.domain.UseCase
import com.mobile.fodein.domain.interfaces.IHearMessage
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.IFormRepository
import com.mobile.fodein.models.exception.DatabaseOperationException
import io.reactivex.Observable
import javax.inject.Inject

class UpdateFormUploadUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                  postExecutionThread: IPostExecutionThread,
                                                  private var formRepository:
                                                  IFormRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread),
        IHearMessage {

    var idForm: String = ""

    override fun hearMessage(): Observable<String> {
        return this.formRepository.userGetMessage()
    }

    override fun hearError(): Observable<DatabaseOperationException> {
        return this.formRepository.userGetError()
    }

    override fun buildUseCaseObservable(): Observable<Boolean> {
        return formRepository.formUpdateUpload(idForm)
    }

}