package com.mobile.fodein.dagger

import com.mobile.fodein.models.executor.*
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(ModelsModule::class))
interface ModelsComponent {
    fun inject(userExecutor: UserExecutor)
    fun inject(districtExecutor: DistrictExecutor)
    fun inject(formExecutor: FormExecutor)
    fun inject(projectExecutor: ProjectExecutor)
    fun inject(unityExecutor: UnityExecutor)
    fun inject(imageExecutor: ImageExecutor)
}

