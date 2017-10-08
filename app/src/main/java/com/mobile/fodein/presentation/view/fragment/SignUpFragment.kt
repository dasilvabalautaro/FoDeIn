package com.mobile.fodein.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import com.mobile.fodein.R


class SignUpFragment: AuthenticateFragment() {

    @BindView(R.id.et_name)
    @JvmField var etName: EditText? = null
    @BindView(R.id.et_user)
    @JvmField var etUser: EditText? = null
    @BindView(R.id.et_email)
    @JvmField var etEmail: EditText? = null
    @BindView(R.id.et_phone)
    @JvmField var etPhone: EditText? = null
    @BindView(R.id.et_password)
    @JvmField var etPassword: EditText? = null
    @BindView(R.id.et_repeat_password)
    @JvmField var etRepeatPassword: EditText? = null
    @BindView(R.id.bt_send)
    @JvmField var btSend: Button? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root: View = inflater!!.inflate(R.layout.view_sign_up,
                container,false)
        ButterKnife.bind(this, root)

        return root
    }
}