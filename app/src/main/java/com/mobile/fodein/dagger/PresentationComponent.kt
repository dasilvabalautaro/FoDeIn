package com.mobile.fodein.dagger

import com.mobile.fodein.presentation.view.fragment.UserFragment
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(PresentationModule::class))
interface PresentationComponent {
    fun inject(userFragment: UserFragment)
}