package com.mobile.fodein.domain.interactor

import com.mobile.fodein.domain.UseCase
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.IImageRepository
import com.mobile.fodein.presentation.model.ImageModel
import io.reactivex.Observable
import javax.inject.Inject

class GetImageListForUpdateUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                       postExecutionThread: IPostExecutionThread,
                                                       private var imageRepository:
                                                       IImageRepository):
        UseCase<List<ImageModel>>(threadExecutor, postExecutionThread){
    override fun buildUseCaseObservable(): Observable<List<ImageModel>> {
        return imageRepository.imageGetByFieldBoolean(false, "upload")
    }


}