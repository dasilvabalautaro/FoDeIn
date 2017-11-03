package com.mobile.fodein.domain.interactor

import com.mobile.fodein.domain.UseCase
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.IDistrictRepository
import io.reactivex.Observable
import javax.inject.Inject


class AddUnitsDistrictListUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                      postExecutionThread: IPostExecutionThread,
                                                      private var districtRepository:
                                                      IDistrictRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread){
    override fun buildUseCaseObservable(): Observable<Boolean> {
        return districtRepository.addUnities()
    }


}