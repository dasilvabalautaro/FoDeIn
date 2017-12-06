package com.mobile.fodein.presentation.view.component

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.mobile.fodein.presentation.view.fragment.BaseFragment
import com.mobile.fodein.presentation.view.fragment.FormFragment
import com.mobile.fodein.presentation.view.fragment.ProjectFragment
import com.mobile.fodein.presentation.view.fragment.UnityFragment


class MainListAdapter(manager: FragmentManager,
                      pager: ViewPager):
        FragmentStatePagerAdapter(manager), BaseFragment.CallbackSelect {

    var pager: ViewPager? = null
    var unity: UnityFragment? = null
    var proyect: ProjectFragment? = null
    var form: FormFragment? = null

    init {
        this.pager = pager

    }

    override fun select(fragment: BaseFragment) {
        if (unity == fragment){
            pager!!.setCurrentItem(1, true)
            proyect!!.showRetry()
        }
        if (proyect == fragment){
            pager!!.setCurrentItem(2, true)
            form!!.showRetry()
        }
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0 ->{
                if (unity == null) unity = UnityFragment()
                unity!!.callbackSelect = this
                return unity!!

            }
            1 ->{
                if (proyect == null) proyect = ProjectFragment()
                proyect!!.callbackSelect = this
                return proyect!!

            }
            2 ->{
                if (form == null) form = FormFragment()

            }

        }
        return form!!
    }

    override fun getCount(): Int {
        return 3
    }


}