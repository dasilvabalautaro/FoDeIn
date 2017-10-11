package com.mobile.fodein.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import com.mobile.fodein.App
import com.mobile.fodein.dagger.PresentationModule
import com.mobile.fodein.presentation.model.UserModel
import com.mobile.fodein.presentation.presenter.UserLoginPresenter
import com.mobile.fodein.presentation.presenter.UserPresenter
import com.mobile.fodein.presentation.view.IUserDetailsView
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class AuthenticateFragment: Fragment(),
        IUserDetailsView {
    protected var disposable: CompositeDisposable = CompositeDisposable()

    val Fragment.app: App
        get() = activity.application as App

    private val component by lazy { app.
            getAppComponent().plus(PresentationModule(context))}

    @Inject
    lateinit var userPresenter: UserPresenter
    @Inject
    lateinit var userLoginPresenter: UserLoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.userPresenter.view = this
        this.userPresenter.hearMessage()
        this.userPresenter.hearError()
        this.userLoginPresenter.view = this
        this.userLoginPresenter.hearMessage()
        this.userLoginPresenter.hearError()
    }

    fun Context.toast(message: CharSequence,
                      duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()

    }

    override fun showError(message: String) {
        context.toast(message)
    }

    override fun showMessage(message: String) {
        context.toast(message)
    }

    override fun renderUser(user: UserModel?) {
        if (user != null){
            context.toast(user.name)
        }
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideRetry() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showRetry() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun context(): Context {
        return activity.applicationContext
    }

}