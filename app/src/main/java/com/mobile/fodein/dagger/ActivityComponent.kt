package com.mobile.fodein.dagger

import com.mobile.fodein.presentation.BaseActivity
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun inject(baseActivity: BaseActivity)
}