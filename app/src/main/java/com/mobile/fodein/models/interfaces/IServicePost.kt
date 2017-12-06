package com.mobile.fodein.models.interfaces

import com.mobile.fodein.models.persistent.network.MessageOfService
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*


interface IServicePost {
    @Headers("Cache-Control: no-cache")
    @POST
    fun sendPost(@Header("Cookie") cookie: String,
                 @Url url: String, @Body body: RequestBody):
            Observable<MessageOfService>
}