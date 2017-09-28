package com.mobile.fodein.dagger

import android.content.Context
import com.mobile.fodein.App
import com.mobile.fodein.domain.interfaces.IPostExecutionThread
import com.mobile.fodein.domain.interfaces.IThreadExecutor
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(app: App)
    fun context(): Context
    fun threadExecutor(): IThreadExecutor
    fun postExecutionThread(): IPostExecutionThread
    fun plus(modelsModule: ModelsModule): ModelsComponent
}