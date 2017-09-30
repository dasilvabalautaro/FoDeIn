package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.domain.interactor.GetUserNewUseCase
import com.mobile.fodein.models.data.User
import com.mobile.fodein.presentation.view.IUserDetailsView
import com.mobile.fodein.tools.Constants
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

class UserPresenter @Inject constructor(private val getUserNewUseCase:
                                        GetUserNewUseCase):
        IPresenter {

    var view: IUserDetailsView? = null
    private var user: User? = null

    fun create(){
        getUserNewUseCase.user = this.user
        getUserNewUseCase.execute(UserObserver())
    }

    fun setDataUser(data: MutableMap<String, Any>){
        this.user = User(data[Constants.USER_NAME].toString(),
                data[Constants.USER_USER].toString(),
                data[Constants.USER_IDCARD].toString(),
                data[Constants.USER_EMAIL].toString(),
                data[Constants.USER_PASSWORD].toString(),
                data[Constants.USER_PHONE].toString(),
                data[Constants.USER_ADDRESS].toString(),
                data[Constants.USER_DESCRIPTION].toString(),
                data[Constants.USER_ROLL].toString(),
                data[Constants.USER_UNIT].toString())
    }

    override fun resume() {
    }

    override fun pause() {
    }

    fun showUserDetailsInView(user: User){
        this.view!!.renderUser(user)
    }

    override fun destroy() {
        this.getUserNewUseCase.dispose()
        view = null
    }

    inner class UserObserver : DisposableObserver<User>() {
        override fun onNext(t: User) {
            showUserDetailsInView(t)
        }

        override fun onComplete() {

        }

        override fun onError(e: Throwable) {
            println(e.message)
        }

    }
}