package com.mobile.fodein.tools

import android.annotation.SuppressLint
import android.app.Application
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.view.ContextThemeWrapper
import java.util.*


class LocaleUtils {
    private var locale: Locale? = null

    public fun setLocale(locale: Locale){
        this.locale = locale
        if (this.locale != null){
            Locale.setDefault(this.locale)
        }
    }
    public fun updateConfiguration(wrapper: ContextThemeWrapper){
        if (locale != null && Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.JELLY_BEAN_MR1){
            val configuration: Configuration = Configuration()
            configuration.setLocale(locale)
            wrapper.applyOverrideConfiguration(configuration)
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    public fun updateConfiguration(app: Application,
                                   configurationNew: Configuration){
        if (locale != null && Build.VERSION.SDK_INT <
                Build.VERSION_CODES.JELLY_BEAN_MR1){
            val configuration: Configuration = Configuration(configurationNew)
            configuration.setLocale(locale)
            val resources: Resources = app.baseContext.resources
            @Suppress("DEPRECATION")
            resources.updateConfiguration(configuration, resources.displayMetrics)

        }

    }

}