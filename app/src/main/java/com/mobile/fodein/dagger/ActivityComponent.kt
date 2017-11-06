package com.mobile.fodein.dagger

import com.mobile.fodein.presentation.view.activities.AccessActivity
import com.mobile.fodein.presentation.view.activities.MainActivity
import com.mobile.fodein.presentation.view.activities.MapActivity
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun inject(accessActivity: AccessActivity)
    fun inject(mapActivity: MapActivity)
    fun inject(mainActivity: MainActivity)
}