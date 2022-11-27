package com.example.marvelapi.helpers.network

/**
 *
 * State hierarchy for different Network Status connections
 * Creditos ao crocsandcoffee - Medium
 * @see <a href="https://crocsandcoffee.medium.com/">Link</a>
 * */
sealed class NetworkStatusState {

    /* Device has a valid internet connection */
    object NetworkStatusConnected : NetworkStatusState()

    /* Device has no internet connection */
    object NetworkStatusDisconnected : NetworkStatusState()
}