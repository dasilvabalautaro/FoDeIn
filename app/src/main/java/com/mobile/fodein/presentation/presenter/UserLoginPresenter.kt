package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.domain.interactor.GetUserLoginUseCase
import com.mobile.fodein.presentation.model.UserModel
import com.mobile.fodein.presentation.view.IUserDetailsView
import com.mobile.fodein.tools.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class UserLoginPresenter @Inject constructor(private val getUserLoginUseCase:
                                             GetUserLoginUseCase):
        IPresenter {

    private var disposable: CompositeDisposable = CompositeDisposable()
    private var password: String = ""
    var view: IUserDetailsView? = null
    private val context = App.appComponent.context()

    fun create(){
        getUserLoginUseCase.execute(UserListObserver())
    }

    fun setUser(data: MutableMap<String, Any>){
        password = data[Constants.USER_PASSWORD].toString()
        getUserLoginUseCase.setUser(data)
    }

    private fun showUsersCollectionInView(usersModelCollection:
                                          Collection<UserModel>){

        if (usersModelCollection.isEmpty()){
            showErrorDetailsInView(context.getString(R.string.register_not_found))
            return
        }

        usersModelCollection.forEach({ user: UserModel ->
            when (password) {
                user.password -> {
                    showUserDetailsInView(user)
                    return
                }
            }
        })

        showErrorDetailsInView(context.getString(R.string.register_not_found))

    }

    private fun showUserDetailsInView(user: UserModel){
        this.view!!.renderUser(user)
    }

    fun showErrorDetailsInView(message: String){
        this.view!!.showError(message)
    }

    override fun resume() {

    }

    override fun pause() {

    }

    fun hearMessage(){
        val hear = getUserLoginUseCase.hearMessage()
        disposable.add(hear.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    showUserMessage(s)
                })
    }

    fun hearError(){
        val hear = getUserLoginUseCase.hearError()
        disposable.add(hear.observeOn(AndroidSchedulers.mainThread())
                .subscribe { e ->
                    showErrorDetailsInView(e.message)
                })

    }

    private fun showUserMessage(message: String){
        this.view!!.showMessage(message)
    }

    override fun destroy() {
        this.getUserLoginUseCase.dispose()
        this.view = null
        if (!this.disposable.isDisposed ) this.disposable.dispose()
    }

    inner class UserListObserver: DisposableObserver<List<UserModel>>(){
        override fun onNext(t: List<UserModel>) {
            showUsersCollectionInView(t)
        }

        override fun onComplete() {

        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showErrorDetailsInView(e.message!!)
            }
        }
    }
}