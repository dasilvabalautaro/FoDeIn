package com.mobile.fodein.dagger

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContextModule(private val context: Context) {

    @Provides
    @FodeinScope
    fun provideContext(): Context{
        return context
    }

}