package com.mobile.fodein.dagger

import android.content.Context
import com.mobile.fodein.models.executor.DatabaseListenerExecutor
import com.mobile.fodein.presentation.mapper.*
import dagger.Module
import dagger.Provides

@Module
class ModelsModule(val context: Context) {

    @Provides
    fun provideInteractDatabaseListener(): DatabaseListenerExecutor {
        return DatabaseListenerExecutor()
    }

    @Provides
    fun provideImageModelDataMapper(): ImageModelDataMapper{
        return ImageModelDataMapper(context)
    }

    @Provides
    fun provideFormModelDataMapper(): FormModelDataMapper {
        return FormModelDataMapper(context)
    }

    @Provides
    fun provideUserModelDataMapper(formModelDataMapper:
                                   FormModelDataMapper): UserModelDataMapper {
        return UserModelDataMapper(context, formModelDataMapper)
    }

    @Provides
    fun provideProjectModelDataMapper(formModelDataMapper:
                                      FormModelDataMapper): ProjectModelDataMapper {
        return ProjectModelDataMapper(context, formModelDataMapper)
    }

    @Provides
    fun provideUnityModelDataMapper(projectModelDataMapper:
                                    ProjectModelDataMapper): UnityModelDataMapper {
        return UnityModelDataMapper(context, projectModelDataMapper)
    }

    @Provides
    fun provideDistrictModelDataMapper(unityModelDataMapper:
                                       UnityModelDataMapper): DistrictModelDataMapper{
        return DistrictModelDataMapper(context, unityModelDataMapper)
    }



}