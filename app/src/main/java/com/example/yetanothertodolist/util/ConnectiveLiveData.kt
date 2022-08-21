package com.example.yetanothertodolist.util

import android.app.Application
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import com.example.yetanothertodolist.di.ApplicationScope
import javax.inject.Inject

/**
 * Слушатель изменения интернета
 */
@ApplicationScope
class ConnectiveLiveData @Inject constructor(context: Application): LiveData<Boolean>() {

    private lateinit var callback: ConnectivityManager.NetworkCallback
    private val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networks: MutableSet<Network> = HashSet()

    override fun onActive() {
        callback = getCallback()

        val networkRequest = NetworkRequest.Builder().addCapability(NET_CAPABILITY_INTERNET).build()
        cm.registerNetworkCallback(networkRequest, callback)
    }

    private fun getCallback() = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            val networkCapabilities = cm.getNetworkCapabilities(network)
            if (networkCapabilities?.hasCapability(NET_CAPABILITY_INTERNET) == true) {
                networks.add(network)
            }
            postValue(networks.size > 0)
        }

        override fun onLost(network: Network) {
            postValue(false)
        }
    }

    override fun onInactive() {
        cm.unregisterNetworkCallback(callback)
        super.onInactive()
    }
}