package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.R
import com.mobile.fodein.domain.interactor.GetUserListUseCase
import com.mobile.fodein.presentation.model.UserModel
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class UserListPresenter @Inject constructor(private val getUserListUseCase:
                                            GetUserListUseCase
                                            ):
        BasePresenter(){

    init {
        this.iHearMessage = getUserListUseCase
    }

    private fun getUserList(){
        this.getUserListUseCase.execute(UserListObserver())
    }

    fun initialize(){
        getUserList()
    }


    private fun showUsersCollectionInView(objectsList:
                                          List<UserModel>){
        this.view!!.renderList(objectsList)
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