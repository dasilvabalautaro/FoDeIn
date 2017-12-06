package com.mobile.fodein.domain.interactor

import com.mobile.fodein.domain.UseCase
import com.mobile.fodein.domain.interfaces.IHearMessage
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.IDistrictRepository
import com.mobile.fodein.models.exception.DatabaseOperationException
import com.mobile.fodein.presentation.model.DistrictModel
import io.reactivex.Observable
import javax.inject.Inject


class GetDistrictListUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                 postExecutionThread: IPostExecutionThread,
                                                 private var districtRepository:
                                                 IDistrictRepository):
        UseCase<List<DistrictModel>>(threadExecutor, postExecutionThread),
        IHearMessage {
    override fun buildUseCaseObservable(): Observable<List<DistrictModel>> =
            districtRepository.districtList()

    override fun hearMessage(): Observable<String> =
            this.districtRepository.userGetMessage()

    override fun hearError(): Observable<DatabaseOperationException> =
            this.districtRepository.userGetError()
}