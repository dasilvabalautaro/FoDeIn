package com.mobile.fodein.presentation.view.component

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.mobile.fodein.presentation.view.fragment.FormFragment
import com.mobile.fodein.presentation.view.fragment.ProjectFragment
import com.mobile.fodein.presentation.view.fragment.UnityFragment


class MainListAdapter(manager: FragmentManager,
                      pager: ViewPager):
        FragmentStatePagerAdapter(manager) {
    var pager: ViewPager? = null
    var unity: UnityFragment? = null
    var proyect: ProjectFragment? = null
    var form: FormFragment? = null

    init {
        this.pager = pager

    }
    override fun getItem(position: Int): Fragment {
        when(position){
            0 ->{
                if (unity == null) unity = UnityFragment()
                return unity!!

            }
            1 ->{
                if (proyect == null) proyect = ProjectFragment()
                return proyect!!

            }
            else ->{
                if (form == null) form = FormFragment()

            }
        }
        return form!!
    }

    override fun getCount(): Int {
        return 3
    }


}