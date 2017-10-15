package com.mobile.fodein.dagger

import com.mobile.fodein.presentation.view.activities.BaseActivity
import com.mobile.fodein.presentation.navigation.Navigator
import com.mobile.fodein.presentation.view.component.ManageImages
import com.mobile.fodein.presentation.view.component.ManageMaps
import com.mobile.fodein.tools.PermissionUtils
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: BaseActivity) {

    @Provides
    fun provideNavigator(): Navigator{
        return Navigator(activity)
    }

    @Provides
    fun provideManageImages(): ManageImages{
        return ManageImages(activity)
    }

    @Provides
    fun providePermissionUtils(): PermissionUtils{
        return PermissionUtils()
    }

    @Provides
    fun provideManageMaps(): ManageMaps{
        return ManageMaps(activity)
    }
}