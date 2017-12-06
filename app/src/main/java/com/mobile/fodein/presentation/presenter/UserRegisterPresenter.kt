package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.R
import com.mobile.fodein.domain.interactor.GetUserLoginUseCase
import com.mobile.fodein.domain.interactor.GetUserNewUseCase
import com.mobile.fodein.presentation.model.UserModel
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

class UserRegisterPresenter @Inject constructor(private val getUserLoginUseCase:
                                                GetUserLoginUseCase,
                                                private val getUserNewUseCase:
                                                GetUserNewUseCase):
        BasePresenter(){
    private var user: UserModel = UserModel()

    init {
        this.iHearMessage = getUserLoginUseCase
    }

    fun verifyRegister(){
        getUserLoginUseCase.execute(UserListObserver())
    }

    fun setUser(data: UserModel){
        this.user = data
        getUserLoginUseCase.setUser(data)
    }

    private fun showUsersCollectionInView(usersModelCollection:
                                          Collection<UserModel>){

        if (usersModelCollection.isEmpty()){
            getUserNewUseCase.setUser(this.user)
            getUserNewUseCase.execute(UserObserver())
        }

    }

    fun showUserDetailsInView(user: UserModel){
        showMessage(context.resources.getString(R.string.task_complete) +
        ": " + user.id)
    }

    override fun destroy() {
        super.destroy()
        this.getUserLoginUseCase.dispose()
        this.getUserNewUseCase.dispose()
    }


    inner class UserListObserver: DisposableObserver<List<UserModel>>(){
        override fun onNext(t: List<UserModel>) {
            showUsersCollectionInView(t)
        }

        override fun onComplete() {
            showMessage(context.resources.getString(R.string.task_complete))
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

    inner class UserObserver : DisposableObserver<UserModel>() {
        override fun onNext(t: UserModel) {
            showUserDetailsInView(t)
        }

        override fun onComplete() {
            showMessage(context.resources.getString(R.string.task_complete))
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }

    }
}