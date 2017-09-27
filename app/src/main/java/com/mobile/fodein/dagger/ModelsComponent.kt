package com.mobile.fodein.dagger

import com.mobile.fodein.models.interactors.InteractUser
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(ModelsModule::class))
interface ModelsComponent {
    fun inject(interactUser: InteractUser)
}