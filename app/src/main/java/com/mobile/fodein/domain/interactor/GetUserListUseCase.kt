package com.mobile.fodein.domain.interactor

import com.mobile.fodein.domain.UseCase
import com.mobile.fodein.domain.interfaces.IHearMessage
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.IUserRepository
import com.mobile.fodein.models.exception.DatabaseOperationException
import com.mobile.fodein.presentation.model.UserModel
import io.reactivex.Observable
import javax.inject.Inject


class GetUserListUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                             postExecutionThread: IPostExecutionThread,
                                             private var userRepository: IUserRepository):
        UseCase<List<UserModel>>(threadExecutor, postExecutionThread),
        IHearMessage {

    override fun buildUseCaseObservable(): Observable<List<UserModel>> {
        return this.userRepository.userList()
    }
    override fun hearError(): Observable<DatabaseOperationException> {
        return this.userRepository.userGetError()
    }

    override fun hearMessage(): Observable<String> {
        return this.userRepository.userGetMessage()
    }

}