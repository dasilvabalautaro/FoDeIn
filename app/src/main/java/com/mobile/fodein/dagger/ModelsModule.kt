package com.mobile.fodein.dagger

import com.mobile.fodein.models.executor.DatabaseListenerExecutor
import com.mobile.fodein.presentation.mapper.*
import dagger.Module
import dagger.Provides

@Module
class ModelsModule {

    @Provides
    fun provideInteractDatabaseListener(): DatabaseListenerExecutor {
        return DatabaseListenerExecutor()
    }

    @Provides
    fun provideUserModelDataMapper(): UserModelDataMapper {
        return UserModelDataMapper()
    }

    @Provides
    fun provideDistrictModelDataMapper(): DistrictModelDataMapper{
        return DistrictModelDataMapper()
    }

    @Provides
    fun provideFormModelDataMapper(): FormModelDataMapper {
        return FormModelDataMapper()
    }
    @Provides
    fun provideProjectModelDataMapper(): ProjectModelDataMapper {
        return ProjectModelDataMapper()
    }
    @Provides
    fun provideUnityModelDataMapper(): UnityModelDataMapper {
        return UnityModelDataMapper()
    }

}