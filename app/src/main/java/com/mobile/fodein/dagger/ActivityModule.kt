package com.mobile.fodein.dagger

import com.mobile.fodein.presentation.BaseActivity
import com.mobile.fodein.presentation.navigation.Navigator
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: BaseActivity) {

    @Provides
    fun provideNavigator(): Navigator{
        return Navigator(activity)
    }
}