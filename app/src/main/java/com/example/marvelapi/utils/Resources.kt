package com.example.marvelapi.utils

/**
 *
 *
 *
 * created on 23/11/2022
 * @author Lucas Goncalves
 */
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}

/**
 *
 *
 * created on 03/10/2022
 * @author Lucas Goncalves
 */
inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Erro Desconhecido")
    }
}