package com.mobile.fodein.presentation.view.component

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.mobile.fodein.presentation.view.fragment.AuthenticateFragment
import com.mobile.fodein.presentation.view.fragment.PhotoUserFragment
import com.mobile.fodein.presentation.view.fragment.SignInFragment
import com.mobile.fodein.presentation.view.fragment.SignUpFragment


class AuthenticateAdapter(manager: FragmentManager,
                          pager: ViewPager):
        FragmentStatePagerAdapter(manager), AuthenticateFragment.Callback {

    override fun remove(fragment: AuthenticateFragment) {
        if (photoUser != fragment){
            pager!!.setCurrentItem(2, true)
        }
    }

    var pager: ViewPager? = null
    var signUp: AuthenticateFragment? = null
    var signIn: AuthenticateFragment? = null
    var photoUser: AuthenticateFragment? = null

    init {
        this.pager = pager
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0 ->{

                if (signIn == null) signIn = SignInFragment()
                signIn!!.callback = this
                return signIn!!

            }
            1 ->{
                if (signUp == null) signUp = SignUpFragment()
                signUp!!.callback = this
                return signUp!!
            }
            else ->{
                if (photoUser == null) photoUser = PhotoUserFragment()
                photoUser!!.callback = this
            }
        }
        return photoUser!!
    }

    override fun getCount(): Int {
        return 3
    }
}