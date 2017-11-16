package com.mobile.fodein.domain.interactor

import com.mobile.fodein.domain.UseCase
import com.mobile.fodein.domain.data.MapperImage
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.IImageRepository
import io.reactivex.Observable
import javax.inject.Inject

class AddImageListUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                              postExecutionThread: IPostExecutionThread,
                                              private var imageRepository:
                                              IImageRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread){

    var list: List<MapperImage> = ArrayList()

    override fun buildUseCaseObservable():
            Observable<Boolean> = imageRepository.addListImage(list)

}