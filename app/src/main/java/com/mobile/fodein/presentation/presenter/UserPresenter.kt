package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.domain.interactor.GetUserNewUseCase
import com.mobile.fodein.presentation.model.UserModel
import com.mobile.fodein.presentation.view.IUserDetailsView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

class UserPresenter @Inject constructor(private val getUserNewUseCase:
                                        GetUserNewUseCase):
        IPresenter {

    private var disposable: CompositeDisposable = CompositeDisposable()


    var view: IUserDetailsView? = null

    fun create(){
        getUserNewUseCase.execute(UserObserver())
    }

    fun setUser(data: MutableMap<String, Any>){
        getUserNewUseCase.setUser(data)
    }

    fun hearMessage(){
        val hear = getUserNewUseCase.hearMessage()
        disposable.add(hear.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    showUserMessage(s)
                })
    }

    fun hearError(){
        val hear = getUserNewUseCase.hearError()
        disposable.add(hear.observeOn(AndroidSchedulers.mainThread())
                .subscribe { e ->
                    showErrorDetailsInView(e.message)
                })

    }

    private fun showUserMessage(message: String){
        this.view!!.showMessage(message)
    }

    override fun resume() {
    }

    override fun pause() {
    }

    fun showUserDetailsInView(user: UserModel){
        this.view!!.renderUser(user)
    }

    fun showErrorDetailsInView(message: String){
        this.view!!.showError(message)
    }

    override fun destroy() {
        this.getUserNewUseCase.dispose()
        view = null
        if (!this.disposable.isDisposed ) this.disposable.dispose()
    }

    inner class UserObserver : DisposableObserver<UserModel>() {
        override fun onNext(t: UserModel) {
            showUserDetailsInView(t)
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