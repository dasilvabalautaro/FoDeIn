package com.mobile.fodein.models.persistent.network

import com.mobile.fodein.models.interfaces.IServiceGet


class ServiceRemoteGet: BaseServiceRemote(){
    private val TAG = ServiceRemoteGet::class.java.name!!
    private val ERROR = TAG + ": IllegalArgument"

    fun getService(): IServiceGet {
        return getClient().
                create(IServiceGet::class.java)?:
                throw IllegalArgumentException(ERROR)
    }
}

    