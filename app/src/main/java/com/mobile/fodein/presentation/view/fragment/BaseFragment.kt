package com.mobile.fodein.presentation.view.fragment

import android.content.Context
import android.support.v4.app.Fragment
import android.widget.Toast
import io.reactivex.disposables.CompositeDisposable


abstract class BaseFragment: Fragment() {
    protected var disposable: CompositeDisposable = CompositeDisposable()
    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}