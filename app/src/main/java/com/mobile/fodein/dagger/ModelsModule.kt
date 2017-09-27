package com.mobile.fodein.dagger

import android.content.Context
import com.mobile.fodein.models.data.User
import com.mobile.fodein.models.interactors.InteractDatabaseListener
import com.mobile.fodein.models.interactors.InteractUser
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module
class ModelsModule {
    @Provides
    fun provideUser(): User{
        return User()
    }

    @FodeinScope
    @Provides
    @Inject
    fun provideInteractUser(user: User, context: Context): InteractUser {
        return InteractUser(user, context)
    }

    @FodeinScope
    @Provides
    @Inject
    fun provideInteractDatabaseListener(context: Context): InteractDatabaseListener {
        return InteractDatabaseListener(context)
    }
}