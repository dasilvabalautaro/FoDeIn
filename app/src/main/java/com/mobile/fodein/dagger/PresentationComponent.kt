package com.mobile.fodein.dagger

import com.mobile.fodein.presentation.view.fragment.*
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(PresentationModule::class))
interface PresentationComponent {
    fun inject(userListFragment: UserListFragment)
    fun inject(authenticateFragment: AuthenticateFragment)
    fun inject(unityFragment: UnityFragment)
    fun inject(projectFragment: ProjectFragment)
    fun inject(formFragment: FormFragment)
}