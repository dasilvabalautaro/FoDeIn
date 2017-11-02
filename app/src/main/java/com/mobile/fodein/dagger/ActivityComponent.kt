package com.mobile.fodein.dagger

import com.mobile.fodein.presentation.view.activities.AccessActivity
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun inject(accessActivity: AccessActivity)
}