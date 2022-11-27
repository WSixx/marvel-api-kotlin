package com.example.marvelapi.helpers.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.RemoteException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Creditos ao crocsandcoffee - Medium
 * @see <a href="https://crocsandcoffee.medium.com/">Link</a>
 */
class NetworkStatusRepository(
    context: Context,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val appScope: CoroutineScope,
) {

    private val cm: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var callback: NetworkCallback? = null
    private var receiver: ConnectivityReceiver? = null

    private val _state = MutableStateFlow(getCurrentNetwork())
    val state: StateFlow<NetworkStatusState> = _state

    init {
        _state
            .subscriptionCount
            .map { count -> count > 0 } // map count into active/inactive flag
            .distinctUntilChanged() // only react to true<->false changes
            .onEach { isActive ->
                /** Only subscribe to network callbacks if we have an active subscriber */
                if (isActive) subscribe()
                else unsubscribe()
            }
            .launchIn(appScope)
    }

    /* Simple getter for fetching network connection status synchronously */
    fun hasNetworkConnection() = getCurrentNetwork() == NetworkStatusState.NetworkStatusConnected

    private fun getCurrentNetwork(): NetworkStatusState {
        return try {
            cm.getNetworkCapabilities(cm.activeNetwork)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .let { connected ->
                    if (connected == true) NetworkStatusState.NetworkStatusConnected
                    else NetworkStatusState.NetworkStatusDisconnected
                }
        } catch (e: RemoteException) {
            NetworkStatusState.NetworkStatusDisconnected
        }
    }

    private fun subscribe() {
        // just in case
        if (callback != null || receiver != null) return

        callback = NetworkCallbackImpl().also { cm.registerDefaultNetworkCallback(it) }

        /* emit our initial state */
        emitNetworkState(getCurrentNetwork())
    }

    private fun unsubscribe() {

        if (callback == null && receiver == null) return

        callback?.run { cm.unregisterNetworkCallback(this) }
        callback = null
    }

    private fun emitNetworkState(newState: NetworkStatusState) {
        appScope.launch(mainDispatcher) {
            _state.emit(newState)
        }
    }

    private inner class ConnectivityReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            /** emit the new network state */
            intent
                .getParcelableExtra<NetworkInfo>(ConnectivityManager.EXTRA_NETWORK_INFO)
                ?.isConnectedOrConnecting
                .let { connected ->
                    if (connected == true) emitNetworkState(NetworkStatusState.NetworkStatusConnected)
                    else emitNetworkState(getCurrentNetwork())
                }
        }
    }

    private inner class NetworkCallbackImpl : NetworkCallback() {

        override fun onAvailable(network: Network) =
            emitNetworkState(NetworkStatusState.NetworkStatusConnected)

        override fun onLost(network: Network) =
            emitNetworkState(NetworkStatusState.NetworkStatusDisconnected)
    }
}