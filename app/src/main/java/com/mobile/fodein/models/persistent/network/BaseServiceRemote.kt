package com.mobile.fodein.models.persistent.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


abstract class BaseServiceRemote {
    private var retrofit: Retrofit? = null
    private val BASE_URL = "http://host/"


    private var client = OkHttpClient.Builder()
            .addInterceptor(Interceptor{ chain ->
                val request = chain.request()
                var response: Response? = null
                try {

                    response = chain.proceed(request)
                    if (response.networkResponse() == null){
                        throw IOException()
                    }
                }catch (ie: IOException){
                    println(ie.message)
                }
                return@Interceptor response
            })
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .build()

    fun getClient(): Retrofit {
        if (retrofit == null){
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()

        }
        return retrofit ?: throw IllegalArgumentException()
    }
}