package com.example.marvelapi.utils

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

        //TODO: IGNORE
        const val PRIVATE_KEY = "14ff6736daa788e27ec2de24860f8e55576e321b"
        const val DATABASE_NAME = "fav-characters"
    }
}