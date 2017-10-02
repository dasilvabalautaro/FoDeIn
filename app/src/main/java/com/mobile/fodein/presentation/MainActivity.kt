package com.mobile.fodein.presentation

import com.mobile.fodein.R
import com.mobile.fodein.presentation.view.fragment.UserListFragment

class MainActivity : BaseActivity() {

    override fun onStart() {
        super.onStart()
        navigator.addFragment(R.id.fragmentContainer, UserListFragment())
    }

}



