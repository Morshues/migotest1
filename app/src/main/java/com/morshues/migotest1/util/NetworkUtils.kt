package com.morshues.migotest1.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest

object NetworkUtils {
    fun registerWifiCallback(
        context: Context,
        networkCallback: ConnectivityManager.NetworkCallback
    ) {
        val connectivityManager
                = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun unregisterWifiCallback(
        context: Context,
        networkCallback: ConnectivityManager.NetworkCallback
    ) {
        val connectivityManager
                = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}