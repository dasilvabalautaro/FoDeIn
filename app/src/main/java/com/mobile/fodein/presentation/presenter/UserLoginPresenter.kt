package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.domain.interactor.GetUserLoginUseCase
import com.mobile.fodein.presentation.interfaces.ILoadDataView
import com.mobile.fodein.presentation.model.UserModel
import com.mobile.fodein.tools.Constants
import com.mobile.fodein.tools.HashUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class UserLoginPresenter @Inject constructor(private val getUserLoginUseCase:
                                             GetUserLoginUseCase):
        IPresenter {

    private var disposable: CompositeDisposable = CompositeDisposable()
    private var password: String = ""
    var view: ILoadDataView? = null
    private val context = App.appComponent.context()

    fun create(){
        getUserLoginUseCase.execute(UserListObserver())
    }

    fun setUser(data: MutableMap<String, Any>){
        password = HashUtils.sha256(data[Constants.USER_PASSWORD].toString())
        getUserLoginUseCase.setUser(data)
    }

    private fun showUsersCollectionInView(usersModelCollection:
                                          Collection<UserModel>){

        if (usersModelCollection.isEmpty()){
            showError(context.getString(R.string.register_not_found))
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

        showError(context.getString(R.string.register_not_found))

    }

    private fun showUserDetailsInView(user: UserModel){
        this.view!!.renderObject(user)
    }

    override fun hearMessage(){
        val hear = getUserLoginUseCase.hearMessage()
        disposable.add(hear.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    showMessage(s)
                })
    }

    override fun hearError(){
        val hear = getUserLoginUseCase.hearError()
        disposable.add(hear.observeOn(AndroidSchedulers.mainThread())
                .subscribe { e ->
                    showError(e.message)
                })

    }

    override fun showMessage(message: String) {
        this.view!!.showMessage(message)
    }

    override fun showError(error: String) {
        this.view!!.showError(error)
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
            showMessage(context.resources.getString(R.string.task_complete))
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }
}