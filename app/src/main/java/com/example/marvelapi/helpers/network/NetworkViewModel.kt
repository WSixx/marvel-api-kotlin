package com.example.marvelapi.helpers.network

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.marvelapi.MarvelApiApplication
import kotlinx.coroutines.flow.StateFlow

/**
 *
 *
 *
 * created on 26/11/2022
 * @author Lucas Goncalves
 */
@MainThread
class NetworkViewModel(
    private val repo: NetworkStatusRepository,
) : ViewModel() {

    /** [StateFlow] emitting a [NetworkStatusState] every time it changes */
    val networkState: StateFlow<NetworkStatusState> = repo.state

    /* Simple helper/getter for fetching network connection status synchronously */
    fun isDeviceOnline(): Boolean = repo.hasNetworkConnection()

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])

                return NetworkViewModel(
                    (application as MarvelApiApplication).networkRepository) as T
            }
        }
    }
}
