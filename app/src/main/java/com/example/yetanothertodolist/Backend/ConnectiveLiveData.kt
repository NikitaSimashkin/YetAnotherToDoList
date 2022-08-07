package com.example.yetanothertodolist.Backend

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import androidx.lifecycle.LiveData

class ConnectiveLiveData(context: Context): LiveData<Boolean>() {

    private lateinit var callback: ConnectivityManager.NetworkCallback
    private val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager


    override fun onActive() {
        callback = object: ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val networkCapabilities = cm.getNetworkCapabilities(network)
                if (networkCapabilities?.hasCapability(NET_CAPABILITY_INTERNET) == true){
                    postValue(true)
                }
            }

            override fun onLost(network: Network) {
                postValue(false)
            }
        }

        val networkRequest = NetworkRequest.Builder().addCapability(NET_CAPABILITY_INTERNET).build()
        cm.registerNetworkCallback(networkRequest, callback)
    }

    override fun onInactive() {
        cm.unregisterNetworkCallback(callback)
        super.onInactive()
    }
}