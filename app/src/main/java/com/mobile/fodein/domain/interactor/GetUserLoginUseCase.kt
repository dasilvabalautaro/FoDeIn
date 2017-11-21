package com.mobile.fodein.domain.interactor

import com.mobile.fodein.domain.UseCase
import com.mobile.fodein.domain.interfaces.IHearMessage
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.IUserRepository
import com.mobile.fodein.models.exception.DatabaseOperationException
import com.mobile.fodein.presentation.model.UserModel
import com.mobile.fodein.tools.Constants
import io.reactivex.Observable
import javax.inject.Inject


class GetUserLoginUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                              postExecutionThread: IPostExecutionThread,
                                              private var userRepository: IUserRepository):
        UseCase<List<UserModel>>(threadExecutor, postExecutionThread),
        IHearMessage {

    var value: String = ""
    var field: String = ""

    fun setUser(data: MutableMap<String, Any>){
        value = data[Constants.USER_USER].toString()
        field = Constants.USER_USER

    }

    fun setUser(data: UserModel){
        value = data.user
        field = Constants.USER_USER

    }


    override fun buildUseCaseObservable(): Observable<List<UserModel>> {
        return this.userRepository.userGetByField(value, field)
    }

    override fun hearMessage(): Observable<String>{
        return this.userRepository.userGetMessage()
    }

    override fun hearError(): Observable<DatabaseOperationException>{
        return this.userRepository.userGetError()
    }
}