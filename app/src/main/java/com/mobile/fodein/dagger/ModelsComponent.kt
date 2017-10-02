package com.mobile.fodein.dagger

import com.mobile.fodein.models.executor.UserExecutor
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(ModelsModule::class))
interface ModelsComponent {
    fun inject(userExecutor: UserExecutor)
    //fun inject(basePresenter: BasePresenter)
}

