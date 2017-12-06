package com.mobile.fodein.domain.interactor

import com.mobile.fodein.domain.UseCase
import com.mobile.fodein.domain.interfaces.IHearMessage
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.IImageRepository
import com.mobile.fodein.models.exception.DatabaseOperationException
import com.mobile.fodein.presentation.model.ImageModel
import io.reactivex.Observable
import javax.inject.Inject

class GetImageListUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                              postExecutionThread: IPostExecutionThread,
                                              private var imageRepository:
                                              IImageRepository):
        UseCase<List<ImageModel>>(threadExecutor, postExecutionThread),
        IHearMessage {

    var nameField = ""
    var valueField = ""

    override fun hearMessage():
            Observable<String> = imageRepository.userGetMessage()

    override fun hearError():
            Observable<DatabaseOperationException> =
            imageRepository.userGetError()

    override fun buildUseCaseObservable():
            Observable<List<ImageModel>> =
            imageRepository.imageGetByField(valueField, nameField)

}

