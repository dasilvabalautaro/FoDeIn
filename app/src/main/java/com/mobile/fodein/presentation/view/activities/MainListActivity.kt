package com.mobile.fodein.presentation.view.activities

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import com.mobile.fodein.R
import com.mobile.fodein.presentation.view.component.MainListAdapter


class MainListActivity: AppCompatActivity() {

    var pager: ViewPager? = null
    var tab: TabLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pager = ButterKnife.findById(this, R.id.vp_list)
        pager!!.adapter = MainListAdapter(supportFragmentManager, pager!!)
        tab = ButterKnife.findById(this, R.id.tl_options)

    }

    override fun onStart() {
        super.onStart()
        tab!!.addTab(tab!!.newTab().setIcon(R.drawable.ic_group_work))
        tab!!.addTab(tab!!.newTab().setIcon(R.drawable.ic_project))
        tab!!.addTab(tab!!.newTab().setIcon(R.drawable.ic_form))
        tab!!.setupWithViewPager(pager)
        tab!!.tabGravity = TabLayout.GRAVITY_CENTER
        tab!!.tabMode = TabLayout.MODE_SCROLLABLE
    }
}

