package com.mobile.fodein.dagger

import com.mobile.fodein.models.executor.DatabaseListenerExecutor
import dagger.Module
import dagger.Provides

@Module
class ModelsModule {

    @Provides
    fun provideInteractDatabaseListener(): DatabaseListenerExecutor {
        return DatabaseListenerExecutor()
    }
}