package com.mobile.fodein.dagger

import com.mobile.fodein.App
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, ContextModule::class))
@FodeinScope
interface AppComponent {
    fun inject(app: App)
    fun plus(modelsModule: ModelsModule): ModelsComponent
}