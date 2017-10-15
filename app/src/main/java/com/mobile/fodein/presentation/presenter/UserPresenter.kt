package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.domain.interactor.GetUserNewUseCase
import com.mobile.fodein.presentation.model.UserModel
import com.mobile.fodein.presentation.interfaces.ILoadDataView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

class UserPresenter @Inject constructor(private val getUserNewUseCase:
                                        GetUserNewUseCase):
        IPresenter {

    private var disposable: CompositeDisposable = CompositeDisposable()
    private val context = App.appComponent.context()
    var view: ILoadDataView? = null

    fun create(){
        getUserNewUseCase.execute(UserObserver())
    }

    fun setUser(data: MutableMap<String, Any>){
        getUserNewUseCase.setUser(data)
    }

    override fun hearMessage(){
        val hear = getUserNewUseCase.hearMessage()
        disposable.add(hear.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    showMessage(s)
                })
    }

    override fun hearError(){
        val hear = getUserNewUseCase.hearError()
        disposable.add(hear.observeOn(AndroidSchedulers.mainThread())
                .subscribe { e ->
                    showError(e.message)
                })

    }

    fun showUserDetailsInView(user: UserModel){
        this.view!!.renderObject(user)
    }

    override fun showMessage(message: String) {
        this.view!!.showMessage(message)
    }

    override fun showError(error: String) {
        this.view!!.showError(error)
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
            showMessage(context.resources.getString(R.string.task_complete))
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }

    }
}