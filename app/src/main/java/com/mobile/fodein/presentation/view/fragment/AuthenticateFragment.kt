package com.mobile.fodein.presentation.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import com.mobile.fodein.App
import com.mobile.fodein.dagger.PresentationModule
import com.mobile.fodein.domain.DeliveryOfResource
import com.mobile.fodein.presentation.interfaces.ILoadDataView
import com.mobile.fodein.presentation.model.UserModel
import com.mobile.fodein.presentation.presenter.*
import com.mobile.fodein.presentation.view.activities.MainListActivity
import com.mobile.fodein.tools.Constants
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject

abstract class AuthenticateFragment: Fragment(),
        ILoadDataView {
    companion object Factory{
        var userImage: Bitmap? = null
        var imageBase64: String = ""
        val pack: MutableMap<String, Any> = HashMap()
        var flagLoginNetwork = false
    }
    protected var disposable: CompositeDisposable = CompositeDisposable()

    interface Callback{
        fun remove(fragment: AuthenticateFragment)
    }

    val Fragment.app: App
        get() = activity.application as App

    private val component by lazy { app.
            getAppComponent().plus(PresentationModule(context))}


    var callback: Callback? = null
    var flagRegister = false

    @Inject
    lateinit var userPresenter: UserPresenter
    @Inject
    lateinit var userLoginPresenter: UserLoginPresenter
    @Inject
    lateinit var userLoginNetworkPresenter: UserLoginNetworkPresenter
    /*@Inject
    lateinit var connectionNetwork: ConnectionNetwork*/
    @Inject
    lateinit var userRegisterNetworkPresenter: UserRegisterNetworkPresenter
    @Inject
    lateinit var userRegisterPresenter: UserRegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.userPresenter.view = this
        this.userLoginPresenter.view = this
        this.userLoginNetworkPresenter.view = this
        this.userRegisterNetworkPresenter.view = this
        this.userRegisterPresenter.view = this
        /*context.toast(getString(R.string.lbl_connect_network) +
                (activity.application as App)
                        .connectionNetwork.checkConnect().toString())*/
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

    override fun <T> renderObject(obj: T) {
        if (obj != null){
            context.toast((obj as UserModel).name)
            DeliveryOfResource.token = (obj as UserModel).token
            DeliveryOfResource.userId = (obj as UserModel).id
            if (flagRegister && (activity.application as App)
                    .connectionNetwork.checkConnect()){
                pack[Constants.USER_ID] = DeliveryOfResource.userId
                this.userRegisterNetworkPresenter.setUser(pack)
                this.userRegisterNetworkPresenter.registerUser()
            }else{
                if (flagLoginNetwork){
                    flagLoginNetwork = false
                    userRegisterPresenter.setUser((obj as UserModel))
                    userRegisterPresenter.verifyRegister()
                }
                activity.navigate<MainListActivity>()
                activity.finish()
            }

        }
    }

    override fun <T> renderList(objectList: List<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        flagRegister = true
    }

    override fun context(): Context = activity.applicationContext

    private inline fun <reified T : Activity> Activity.navigate() {
        val intent = Intent(activity, T::class.java)
        startActivity(intent)
    }
}