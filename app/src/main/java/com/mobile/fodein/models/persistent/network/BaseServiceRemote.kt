package com.mobile.fodein.models.persistent.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

abstract class BaseServiceRemote {
    private var retrofit: Retrofit? = null
    private val BASE_URL = "http://host/"


    fun getClient(): Retrofit {
        if (retrofit == null){
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

        }
        return retrofit ?: throw IllegalArgumentException()
    }
}