package com.mobile.fodein.models.persistent.network

import com.mobile.fodein.models.interfaces.IServicePost

class ServiceRemotePost: BaseServiceRemote() {
    private val TAG = ServiceRemotePost::class.java.name!!
    private val ERROR = TAG + ": IllegalArgument"

    fun getService(): IServicePost {
        return getClient().
                create(IServicePost::class.java)?:
                throw IllegalArgumentException(ERROR)
    }
}