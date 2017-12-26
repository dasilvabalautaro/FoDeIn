package com.mobile.fodein.models.interfaces

import com.mobile.fodein.models.persistent.network.MessageOfService
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Url


interface IServiceGet {
    @Headers("Cache-Control: no-cache")
    @GET
    fun sendGet(@Header("Cookie") cookie: String,
                 @Url url: String):
            Single<MessageOfService>
}