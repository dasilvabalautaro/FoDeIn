package com.mobile.fodein.presentation.presenter

import com.mobile.fodein.R
import com.mobile.fodein.domain.interactor.GetUserLoginUseCase
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.model.UserModel
import com.mobile.fodein.tools.Constants
import com.mobile.fodein.tools.HashUtils
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class UserLoginPresenter @Inject constructor(private val getUserLoginUseCase:
                                             GetUserLoginUseCase):
        BasePresenter() {

    private var password: String = ""

    init {
        this.iHearMessage = getUserLoginUseCase
    }

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
                    CachingLruRepository.instance.getLru()
                            .put(Constants.CACHE_USER_MODEL, user)
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

    override fun destroy() {
        super.destroy()
        this.getUserLoginUseCase.dispose()
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