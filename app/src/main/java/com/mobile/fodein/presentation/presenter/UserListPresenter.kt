package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.domain.interactor.GetUserListUseCase
import com.mobile.fodein.presentation.model.UserModel
import com.mobile.fodein.presentation.view.IUserListView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class UserListPresenter @Inject constructor(private val getUserListUseCase:
                                            GetUserListUseCase
                                            ): IPresenter  {
    private var disposable: CompositeDisposable = CompositeDisposable()

    var view: IUserListView? = null

    override fun resume() {

    }

    override fun pause() {

    }

    private fun getUserList(){
        this.getUserListUseCase.execute(UserListObserver())
    }

    fun initialize(){
        getUserList()
    }

    override fun destroy() {
        this.getUserListUseCase.dispose()
        this.view = null
        if (!this.disposable.isDisposed ) this.disposable.dispose()
    }

    private fun showUsersCollectionInView(usersModelColletion: Collection<UserModel>){
        this.view!!.renderUserList(usersModelColletion)
    }
    fun showErrorDetailsInView(message: String){
        this.view!!.showError(message)
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