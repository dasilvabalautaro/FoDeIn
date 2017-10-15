package com.mobile.fodein.presentation.view.activities

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.dagger.ActivityModule
import com.mobile.fodein.presentation.navigation.Navigator
import com.mobile.fodein.presentation.view.component.ManageImages
import com.mobile.fodein.presentation.view.component.ManageMaps
import com.mobile.fodein.tools.PermissionUtils
import javax.inject.Inject


abstract class BaseActivity: AppCompatActivity(){

    val Activity.app: App
        get() = application as App

    private val component by lazy { app.getAppComponent()
            .plus(ActivityModule(this))}

    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var manageImages: ManageImages
    @Inject
    lateinit var permissionUtils: PermissionUtils
    @Inject
    lateinit var manageMaps: ManageMaps

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        component.inject(this)
    }

    open fun Activity.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}