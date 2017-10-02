package com.mobile.fodein.dagger

import com.mobile.fodein.presentation.view.fragment.UserFragment
import com.mobile.fodein.presentation.view.fragment.UserListFragment
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(PresentationModule::class))
interface PresentationComponent {
    fun inject(userFragment: UserFragment)
    fun inject(userListFragment: UserListFragment)
}