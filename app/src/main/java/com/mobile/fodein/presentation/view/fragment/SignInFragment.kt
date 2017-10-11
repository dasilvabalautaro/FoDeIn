package com.mobile.fodein.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
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


class SignInFragment: AuthenticateFragment() {

    private val validation: AwesomeValidation =
            AwesomeValidation(ValidationStyle.BASIC)

    @BindView(R.id.et_user)
    @JvmField var etUser: EditText? = null
    @BindView(R.id.et_password)
    @JvmField var etPassword: EditText? = null
    @BindView(R.id.bt_send)
    @JvmField var btSend: Button? = null

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root: View = inflater!!.inflate(R.layout.view_sign_in,
                container,false)
        ButterKnife.bind(this, root)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setValidationFields()
    }

    override fun onStart() {
        super.onStart()
        disposable.add( actionSingUpButtonClickObservable()
                .observeOn(Schedulers.newThread())
                .map { validate ->
                    run{
                        if (validate){
                            this.userLoginPresenter.setUser(loadPack())
                            this.userLoginPresenter.create()

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

    private fun setValidationFields(){
        val regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])" +
                "(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{6,20}"

        validation.addValidation(activity, R.id.et_user,
                RegexTemplate.NOT_EMPTY, R.string.invalid_name)
        validation.addValidation(activity, R.id.et_password,
                regexPassword, R.string.invalid_password)

    }

    private fun loadPack(): MutableMap<String, Any>{
        val pack: MutableMap<String, Any> = HashMap()
        pack[Constants.USER_USER] = etUser?.text!!.trim().toString()
        pack[Constants.USER_PASSWORD] = etPassword?.text!!.trim().toString()
        return pack
    }


    private fun actionSingUpButtonClickObservable(): Observable<Boolean> {
        return Observable.create({
            e: ObservableEmitter<Boolean>? ->
            btSend!!.setOnClickListener({
                e!!.onNext(validation.validate())
            })
            e!!.setCancellable { Cancellable{
                btSend!!.setOnClickListener(null)
            } }
        })
    }
}