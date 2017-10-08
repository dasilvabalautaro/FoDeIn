package com.mobile.fodein.presentation.view.fragment

import android.content.Context
import android.support.v4.app.Fragment
import android.widget.Toast

abstract class AuthenticateFragment: Fragment() {

    fun Context.toast(message: CharSequence,
                      duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()

    }

}