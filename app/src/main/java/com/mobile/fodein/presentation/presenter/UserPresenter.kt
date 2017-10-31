package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.R
import com.mobile.fodein.domain.interactor.GetUserNewUseCase
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.model.UserModel
import com.mobile.fodein.tools.Constants
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

class UserPresenter @Inject constructor(private val getUserNewUseCase:
                                        GetUserNewUseCase):
        BasePresenter() {

    init {
        this.iHearMessage = getUserNewUseCase
    }

    fun registerUser(){
        getUserNewUseCase.execute(UserObserver())
    }

    fun setUser(data: MutableMap<String, Any>){
        getUserNewUseCase.setUser(data)
    }

    fun showUserDetailsInView(user: UserModel){
        this.view!!.renderObject(user)
    }

    override fun destroy() {
        super.destroy()
        this.getUserNewUseCase.dispose()
    }

    inner class UserObserver : DisposableObserver<UserModel>() {
        override fun onNext(t: UserModel) {
            CachingLruRepository.instance.getLru()
                    .put(Constants.CACHE_USER_MODEL, t)
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