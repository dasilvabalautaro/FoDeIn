package com.mobile.fodein.dagger

import android.app.Application
import android.content.Context
import com.mobile.fodein.models.persistent.PersistentDatabase
import com.mobile.fodein.tools.LocaleUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context{
        return this.app
    }

    @Provides
    fun provideLocaleConfiguration(): LocaleUtils{
        return LocaleUtils()
    }

    @Provides
    fun providePersistentDatabase(): PersistentDatabase {
        return PersistentDatabase(app)
    }
}