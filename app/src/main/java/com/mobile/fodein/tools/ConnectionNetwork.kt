package com.mobile.fodein.tools

import android.content.Context
import android.net.ConnectivityManager


class ConnectionNetwork(val context: Context) {
    fun isOnline(): Boolean {
        val connectivityManager = context.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

}