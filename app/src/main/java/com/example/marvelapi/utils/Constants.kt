package com.example.marvelapi.utils

import com.example.marvelapi.BuildConfig

/**
 *
 *
 *
 * created on 23/11/2022
 * @author Lucas Goncalves
 */
class Constants {
    companion object {
        const val BASE_URL = "https://gateway.marvel.com/v1/public/"
        val timeStamp = System.currentTimeMillis()
        const val PUBLIC_KEY = "a1060a085d3c48b937b63dfcea07593d"

        const val PRIVATE_KEY = BuildConfig.PRIVATE_KEY
        const val DATABASE_NAME = "fav-characters"
    }
}