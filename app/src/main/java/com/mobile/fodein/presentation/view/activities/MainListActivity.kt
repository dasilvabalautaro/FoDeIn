package com.mobile.fodein.presentation.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import butterknife.ButterKnife
import com.mobile.fodein.R
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
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
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        tab!!.setupWithViewPager(pager)
    }

    override fun onStart() {
        super.onStart()
        tab!!.getTabAt(0)!!.setIcon(R.drawable.ic_group_work)
        tab!!.getTabAt(1)!!.setIcon(R.drawable.ic_project)
        tab!!.getTabAt(2)!!.setIcon(R.drawable.ic_form)
        tab!!.tabGravity = TabLayout.GRAVITY_FILL

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId

        if (id == R.id.action_item_map){
            this.navigate<MapActivity>()
            this.finish()

        }
        if (id == R.id.action_item_form){
            this.navigate<MainActivity>()
            this.finish()
        }
        if (id == R.id.action_item_list){

        }

        return super.onOptionsItemSelected(item)

    }


    override fun onBackPressed() {
        super.onBackPressed()
        CachingLruRepository.instance.getLru().evictAll()
    }
    private inline fun <reified T : Activity> Activity.navigate() {
        val intent = Intent(this@MainListActivity, T::class.java)
        startActivity(intent)
    }
    fun Activity.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

