package com.mobile.fodein.presentation.view.component

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.mobile.fodein.presentation.view.fragment.AuthenticateFragment
import com.mobile.fodein.presentation.view.fragment.SignInFragment
import com.mobile.fodein.presentation.view.fragment.SignUpFragment


class AuthenticateAdapter(manager: FragmentManager,
                          pager: ViewPager):
        FragmentStatePagerAdapter(manager) {

    var pager: ViewPager? = null
    var signUp: AuthenticateFragment? = null
    var signIn: AuthenticateFragment? = null

    init {
        this.pager = pager

    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0 ->{

                if (signIn == null) signIn = SignInFragment()
                return signIn!!

            }
            else ->{
                if (signUp == null) signUp = SignUpFragment()

            }
        }
        return signUp!!
    }

    override fun getCount(): Int {
        return 2
    }
}