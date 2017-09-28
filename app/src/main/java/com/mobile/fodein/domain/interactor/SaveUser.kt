package com.mobile.fodein.domain.interactor

import com.mobile.fodein.domain.UseCase
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.domain.repository.IUserRepository
import com.mobile.fodein.models.data.User
import io.reactivex.Observable
import javax.inject.Inject


class SaveUser @Inject constructor(threadExecutor: IThreadExecutor,
                                   postExecutionThread: IPostExecutionThread,
                                   private var userRepository: IUserRepository):
        UseCase<User>(threadExecutor, postExecutionThread) {

    var user: User? = null

    override fun buildUseCaseObservable(): Observable<User> {
        return if (user != null){
            this.userRepository.userSave(user!!)
        }else{
            return Observable.empty()
        }
    }

}