package com.mobile.fodein.presentation

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.dagger.ActivityModule
import com.mobile.fodein.presentation.navigation.Navigator
import javax.inject.Inject


abstract class BaseActivity: AppCompatActivity() {

    val Activity.app: App
        get() = application as App

    private val component by lazy { app.getAppComponent()
            .plus(ActivityModule(this))}

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        component.inject(this)
    }

}