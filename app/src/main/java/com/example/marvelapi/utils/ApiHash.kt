package com.example.marvelapi.utils

import java.math.BigInteger
import java.security.MessageDigest

/**
 *
 *
 *
 * created on 23/11/2022
 * @author Lucas Goncalves
 */
open class ApiHash {

    companion object {
        private const val ALGORITHM = "MD5"

        fun buildAndGetHash(): String {
            val toBeHashing =
                "${Constants.timeStamp}${Constants.PRIVATE_KEY}${Constants.PUBLIC_KEY}"
            val md = MessageDigest.getInstance(ALGORITHM)
            return BigInteger(1, md.digest(toBeHashing.toByteArray())).toString(16)
                .padStart(32, '0')
        }
    }
}