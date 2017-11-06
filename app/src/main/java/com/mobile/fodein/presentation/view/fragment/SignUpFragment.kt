package com.mobile.fodein.presentation.view.fragment

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.mobile.fodein.R
import com.mobile.fodein.tools.Constants
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Cancellable
import io.reactivex.schedulers.Schedulers


class SignUpFragment: AuthenticateFragment() {
    private var validation: AwesomeValidation? = null

    @BindView(R.id.et_name)
    @JvmField var etName: EditText? = null
    @BindView(R.id.et_user)
    @JvmField var etUser: EditText? = null
    @BindView(R.id.et_email)
    @JvmField var etEmail: EditText? = null
    @BindView(R.id.et_mobile)
    @JvmField var etPhone: EditText? = null
    @BindView(R.id.et_password)
    @JvmField var etPassword: EditText? = null
    @BindView(R.id.et_repeat_password)
    @JvmField var etRepeatPassword: EditText? = null
    @BindView(R.id.bt_send)
    @JvmField var btSend: Button? = null
    @OnClick(R.id.ib_photo)
    fun makeTransition(){
        if (callback != null){
            callback!!.remove(this)
        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root: View = inflater!!.inflate(R.layout.view_sign_up,
                container,false)
        ButterKnife.bind(this, root)
        return root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        validation = AwesomeValidation(ValidationStyle.BASIC)
        setValidationFields()
    }
    override fun onStart() {
        super.onStart()
        disposable.add( actionSingUpButtonClickObservable()
                .observeOn(Schedulers.newThread())
                .map { validate ->
                    run{
                        if (validate){
                            this.userPresenter.setUser(loadPack())
                            this.userPresenter.registerUser()
                            return@map resources
                                    .getString(R.string.data_sent)

                        }else{
                            return@map resources
                                    .getString(R.string.invalid_form)
                        }
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result -> context.toast(result)})
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putString("name", etName!!.text.toString())
        outState.putString("user", etUser!!.text.toString())
        outState.putString("email", etEmail!!.text.toString())
        outState.putString("phone", etPhone!!.text.toString())
        outState.putString("password", etPassword!!.text.toString())
        outState.putString("repeatPassword", etRepeatPassword!!.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null){
            etName!!.setText(savedInstanceState.getString("name"))
            etUser!!.setText(savedInstanceState.getString("user"))
            etEmail!!.setText(savedInstanceState.getString("email"))
            etPhone!!.setText(savedInstanceState.getString("phone"))
            etPassword!!.setText(savedInstanceState.getString("password"))
            etRepeatPassword!!.setText(savedInstanceState.getString("repeatPassword"))

        }
    }

    private fun setValidationFields(){
//        val regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])" +
//                "(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{6,}"
        val regexPassword = ".{6,}"
        validation!!.addValidation(activity, R.id.et_name,
                RegexTemplate.NOT_EMPTY, R.string.invalid_name)
        validation!!.addValidation(activity, R.id.et_user,
                RegexTemplate.NOT_EMPTY, R.string.invalid_name)
        validation!!.addValidation(activity, R.id.et_email,
                Patterns.EMAIL_ADDRESS, R.string.invalid_email)
        validation!!.addValidation(activity, R.id.et_mobile,
                "^[+]?[0-9]{8,13}$", R.string.invalid_phone)
        validation!!.addValidation(activity, R.id.et_password,
                regexPassword, R.string.invalid_password)
        validation!!.addValidation(activity, R.id.et_repeat_password,
                R.id.et_password, R.string.invalid_confirm_password)

    }

    private fun loadPack(): MutableMap<String, Any>{
        pack.clear()
        pack[Constants.USER_ID] = ""
        pack[Constants.USER_PASSWORD] = etPassword?.text!!.trim().toString()
        pack[Constants.USER_TOKEN] = ""
        pack[Constants.USER_IMAGE] = imageBase64
        pack[Constants.USER_NAME] = etName?.text!!.trim().toString()
        pack[Constants.USER_USER] = etUser?.text!!.trim().toString()
        pack[Constants.USER_PHONE] = etPhone?.text!!.trim().toString()
        pack[Constants.USER_ADDRESS] = "Plaza Murillo"
        pack[Constants.USER_EMAIL] = etEmail?.text!!.trim().toString()
        pack[Constants.USER_IDCARD] = "1234567"
        pack[Constants.USER_DESCRIPTION] = "Recolector de datos"
        pack[Constants.USER_UNIT] = "1"
        pack[Constants.USER_ROLL] = "Jefe"
        return pack
    }

    private fun actionSingUpButtonClickObservable(): Observable<Boolean> {
        return Observable.create({
            e: ObservableEmitter<Boolean>? ->
            btSend!!.setOnClickListener({
                e!!.onNext(validation!!.validate())
            })
            e!!.setCancellable { Cancellable{
                btSend!!.setOnClickListener(null)
            } }
        })
    }
}