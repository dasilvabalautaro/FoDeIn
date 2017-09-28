package com.mobile.fodein.dagger

import android.app.Application
import android.content.Context
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import com.mobile.fodein.models.executor.JobExecutor
import com.mobile.fodein.models.persistent.PersistentDatabase
import com.mobile.fodein.presentation.UIThread
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

    @Provides
    @Singleton
    fun provideThreadExecutor(jobExecutor: JobExecutor): IThreadExecutor {
        return jobExecutor
    }

    @Provides
    @Singleton
    fun providePostExecutionThread(uiThread: UIThread): IPostExecutionThread {
        return uiThread
    }

}