package com.mobile.fodein.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.dagger.PresentationModule
import com.mobile.fodein.models.data.User
import com.mobile.fodein.presentation.model.UserModel
import com.mobile.fodein.presentation.presenter.UserListPresenter
import com.mobile.fodein.presentation.view.IUserListView
import javax.inject.Inject


class UserListFragment: BaseFragment(), IUserListView {

    val Fragment.app: App
        get() = activity.application as App

    private val component by lazy { app.
            getAppComponent().plus(PresentationModule(context))}

    @Inject
    lateinit var userListPresenter: UserListPresenter

    @BindView(R.id.tv_list)
    @JvmField var tvList: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root: View = inflater!!.inflate(R.layout.fragment_list_users,
                container,false)
        ButterKnife.bind(this, root)

        return root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.userListPresenter.view = this
    }

    override fun onResume() {
        super.onResume()
        this.userListPresenter.initialize()
    }

    override fun renderUserList(userModelCollection: Collection<UserModel>) {
        var res = ""
        for (user: UserModel in userModelCollection) res += user.id + '\n'
        tvList!!.text =  res
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessage(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun viewUser(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun context(): Context {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}