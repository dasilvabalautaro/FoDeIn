package com.mobile.fodein.dagger

import com.mobile.fodein.models.data.User
import com.mobile.fodein.models.interactors.InteractDatabaseListener
import com.mobile.fodein.models.interactors.InteractUser
import dagger.Module
import dagger.Provides

@Module
class ModelsModule {
    @Provides
    fun provideUser(): User{
        return User()
    }

    @Provides
    fun provideInteractUser(user: User): InteractUser {
        return InteractUser(user)
    }

    @Provides
    fun provideInteractDatabaseListener(): InteractDatabaseListener {
        return InteractDatabaseListener()
    }
}