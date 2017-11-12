package com.mobile.fodein.presentation.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.dagger.ActivityModule
import com.mobile.fodein.domain.data.GeographicCoordinates
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.view.component.ManageMaps
import javax.inject.Inject

class MapActivity: AppCompatActivity() {

    val Activity.app: App
        get() = application as App

    private val component by lazy { app.getAppComponent()
            .plus(ActivityModule(this))}
    @Inject
    lateinit var manageMaps: ManageMaps

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_form_map)
        component.inject(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        manageMaps.getMapAsync()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        if (id == R.id.action_item_info){
            //setCoordinates()
        }
        if (id == R.id.action_item_help){

        }
        if (id == R.id.action_item_list){
            this.navigate<MainListActivity>()
            this.finish()
        }

        return super.onOptionsItemSelected(item)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        CachingLruRepository.instance.getLru().evictAll()
    }

    private inline fun <reified T : Activity> Activity.navigate() {
        val intent = Intent(this, T::class.java)
        startActivity(intent)
    }

    fun Activity.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun setCoordinates(){
        val listCoordinates: ArrayList<GeographicCoordinates> = ArrayList()
        val geoCoordinates = GeographicCoordinates()
        geoCoordinates.latitude = -16.482086
        geoCoordinates.longitude = -68.123833
        geoCoordinates.title = "Elias Sagarnaga"
        listCoordinates.add(geoCoordinates)
        val geoCoordinates1 = GeographicCoordinates()
        geoCoordinates1.latitude = -16.486489
        geoCoordinates1.longitude = -68.124241
        geoCoordinates1.title = "Avenida Tejada Sorzano"
        listCoordinates.add(geoCoordinates1)
        val geoCoordinates2 = GeographicCoordinates()
        geoCoordinates2.latitude = -16.477230
        geoCoordinates2.longitude = -68.123919
        geoCoordinates2.title = "Calle 2 Chapuma"
        listCoordinates.add(geoCoordinates2)
        val geoCoordinates3 = GeographicCoordinates()
        geoCoordinates3.latitude = -16.467813
        geoCoordinates3.longitude = -68.113976
        geoCoordinates3.title = "Villa El Carmen"
        listCoordinates.add(geoCoordinates3)
        manageMaps.setCoordinates(listCoordinates)

    }
}