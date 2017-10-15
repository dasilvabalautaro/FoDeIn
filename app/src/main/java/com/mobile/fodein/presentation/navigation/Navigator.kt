package com.mobile.fodein.presentation.navigation

import android.support.v4.app.Fragment
import com.mobile.fodein.presentation.view.activities.BaseActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor(private val activity: BaseActivity){

    fun addFragment(containerViewId: Int, fragment: Fragment){
        activity.supportFragmentManager
                .beginTransaction()
                .replace(containerViewId, fragment)
                .addToBackStack(null)
                .commit()
    }
}