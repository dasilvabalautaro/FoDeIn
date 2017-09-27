package com.mobile.fodein

import android.app.Application
import android.content.res.Configuration
import com.mobile.fodein.dagger.AppComponent
import com.mobile.fodein.dagger.AppModule
import com.mobile.fodein.dagger.DaggerAppComponent
import com.mobile.fodein.models.persistent.PersistentDatabase
import com.mobile.fodein.tools.LocaleUtils
import java.util.*
import javax.inject.Inject


class App: Application() {
    @Inject
    lateinit var localeUtils: LocaleUtils
    @Inject
    lateinit var persistentDatabase: PersistentDatabase

    companion object{
        lateinit var appComponent: AppComponent
    }

    private val component: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }


    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        persistentDatabase.create()
        localeUtils.setLocale(Locale("es"))
        localeUtils.updateConfiguration(this,
                baseContext.resources.configuration)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        localeUtils.updateConfiguration(this, newConfig!!)
    }

    fun getAppComponent(): AppComponent{
        appComponent = component
        return appComponent
    }
}