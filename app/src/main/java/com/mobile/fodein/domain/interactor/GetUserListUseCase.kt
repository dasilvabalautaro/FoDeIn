package com.mobile.fodein.domain.interactor

import com.mobile.fodein.domain.UseCase
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.IUserRepository
import com.mobile.fodein.models.data.User
import com.mobile.fodein.presentation.model.UserModel
import io.reactivex.Observable
import javax.inject.Inject


class GetUserListUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                             postExecutionThread: IPostExecutionThread,
                                             private var userRepository: IUserRepository):
        UseCase<List<UserModel>>(threadExecutor, postExecutionThread)  {
    override fun buildUseCaseObservable(): Observable<List<UserModel>> {
        return this.userRepository.userList()
    }

}