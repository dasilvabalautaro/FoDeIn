package com.mobile.fodein.domain.interactor

import com.mobile.fodein.domain.UseCase
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.IFormRepository
import com.mobile.fodein.presentation.model.FormModel
import io.reactivex.Observable
import javax.inject.Inject

class GetFormListForUpdateUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                      postExecutionThread: IPostExecutionThread,
                                                      private var formRepository:
                                                      IFormRepository):
        UseCase<List<FormModel>>(threadExecutor, postExecutionThread){

    override fun buildUseCaseObservable(): Observable<List<FormModel>> {
        return formRepository.formGetByFieldBoolean(false, "upload")
    }

}