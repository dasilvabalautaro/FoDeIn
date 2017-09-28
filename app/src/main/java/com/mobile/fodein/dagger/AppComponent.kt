package com.mobile.fodein.dagger

import android.content.Context
import com.mobile.fodein.App
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(app: App)
    fun context(): Context
//    fun threadExecutor(): IThreadExecutor
//    fun postExecutionThread(): IPostExecutionThread
//    fun userRepository(): IUserRepository
    fun plus(modelsModule: ModelsModule): ModelsComponent
}