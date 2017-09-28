package com.mobile.fodein.dagger

import com.mobile.fodein.models.data.User
import com.mobile.fodein.models.executor.DatabaseListenerExecutor
import com.mobile.fodein.models.executor.UserExecutor
import dagger.Module
import dagger.Provides

@Module
class ModelsModule {
    @Provides
    fun provideUser(): User{
        return User()
    }

    @Provides
    fun provideInteractUser(user: User): UserExecutor {
        return UserExecutor(user)
    }

    @Provides
    fun provideInteractDatabaseListener(): DatabaseListenerExecutor {
        return DatabaseListenerExecutor()
    }
}