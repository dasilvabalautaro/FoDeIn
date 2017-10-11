package com.mobile.fodein.domain.interactor

import com.mobile.fodein.domain.UseCase
import com.mobile.fodein.domain.data.MapperUser
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.IUserRepository
import com.mobile.fodein.models.exception.DatabaseOperationException
import com.mobile.fodein.presentation.model.UserModel
import com.mobile.fodein.tools.Constants
import io.reactivex.Observable
import javax.inject.Inject


class GetUserNewUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                            postExecutionThread: IPostExecutionThread,
                                            private var userRepository: IUserRepository):
        UseCase<UserModel>(threadExecutor, postExecutionThread) {
    var user: MapperUser = MapperUser()

    fun setUser(data: MutableMap<String, Any>){
        user.name = data[Constants.USER_NAME].toString()
        user.user = data[Constants.USER_USER].toString()
        user.idCard = data[Constants.USER_IDCARD].toString()
        user.email = data[Constants.USER_EMAIL].toString()
        user.password = data[Constants.USER_PASSWORD].toString()
        user.phone = data[Constants.USER_PHONE].toString()
        user.address = data[Constants.USER_ADDRESS].toString()
        user.description = data[Constants.USER_DESCRIPTION].toString()
        user.roll = data[Constants.USER_ROLL].toString()
        user.unit = data[Constants.USER_UNIT].toString()

    }

    override fun buildUseCaseObservable(): Observable<UserModel> {
        return this.userRepository.userSave(user)
    }

    fun hearMessage(): Observable<String>{
        return this.userRepository.userGetMessage()
    }

    fun hearError(): Observable<DatabaseOperationException>{
        return this.userRepository.userGetError()
    }
}