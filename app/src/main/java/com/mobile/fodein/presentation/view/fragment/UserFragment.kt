package com.mobile.fodein.presentation.view.fragment


import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.dagger.PresentationModule
import com.mobile.fodein.domain.data.MapperUser
import com.mobile.fodein.presentation.presenter.UserPresenter
import com.mobile.fodein.presentation.view.IUserDetailsView
import com.mobile.fodein.tools.Constants
import javax.inject.Inject


class UserFragment: BaseFragment(), IUserDetailsView {
    val Fragment.app: App
        get() = activity.application as App

    private val component by lazy { app.
            getAppComponent().plus(PresentationModule(context))}

    @Inject
    lateinit var userPresenter: UserPresenter

    @BindView(R.id.et_name)
    @JvmField var etName: TextInputEditText? = null
    @BindView(R.id.et_user)
    @JvmField var etUser: TextInputEditText? = null
    @BindView(R.id.et_email)
    @JvmField var etEmail: TextInputEditText? = null
    @BindView(R.id.et_password)
    @JvmField var etPassword: TextInputEditText? = null
    @BindView(R.id.et_phone)
    @JvmField var etPhone: TextInputEditText? = null
    @BindView(R.id.et_address)
    @JvmField var etAddress: TextInputEditText? = null
    @OnClick(R.id.bt_save)
    fun save(){
        this.userPresenter.setUser(loadPack())
        this.userPresenter.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root: View = inflater!!.inflate(R.layout.fragment_user,
                container,false)
        ButterKnife.bind(this, root)

        return root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.userPresenter.view = this
        this.userPresenter.hearMessage()
        this.userPresenter.hearError()
    }

    private fun loadPack(): MutableMap<String, Any>{
        val pack: MutableMap<String, Any> = HashMap()
        pack[Constants.USER_NAME] = etName?.text.toString()
        pack[Constants.USER_USER] = etUser?.text.toString()
        pack[Constants.USER_PASSWORD] = etPassword?.text.toString()
        pack[Constants.USER_PHONE] = etPhone?.text.toString()
        pack[Constants.USER_ADDRESS] = etAddress?.text.toString()
        pack[Constants.USER_EMAIL] = etEmail?.text.toString()
        pack[Constants.USER_IDCARD] = "123"
        pack[Constants.USER_DESCRIPTION] = "hola a todos"
        pack[Constants.USER_UNIT] = "Planes"
        pack[Constants.USER_ROLL] = "Jefe"
        return pack
    }

    override fun showLoading() {

    }

    override fun showMessage(message: String) {
        context.toast(message)
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun renderUser(user: MapperUser?){
        if (user != null){
            //context.toast(user.user)
        }
    }


    override fun showRetry() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideRetry() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(message: String) {
        context.toast(message)
    }

    override fun context(): Context {
        return activity.applicationContext
    }

}