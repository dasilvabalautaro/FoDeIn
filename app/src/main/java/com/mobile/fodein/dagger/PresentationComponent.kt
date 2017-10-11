package com.mobile.fodein.dagger

import com.mobile.fodein.presentation.view.fragment.AuthenticateFragment
import com.mobile.fodein.presentation.view.fragment.UserListFragment
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(PresentationModule::class))
interface PresentationComponent {
    fun inject(userListFragment: UserListFragment)
    fun inject(authenticateFragment: AuthenticateFragment)
}