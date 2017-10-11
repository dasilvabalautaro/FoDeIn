package com.mobile.fodein.presentation.view.activities

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import com.mobile.fodein.R
import com.mobile.fodein.presentation.view.component.AuthenticateAdapter

class AccessActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_access)
        val pager: ViewPager = ButterKnife.findById(this, R.id.vp_access)
        pager.adapter = AuthenticateAdapter(supportFragmentManager, pager)

    }
}