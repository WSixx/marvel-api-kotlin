package com.example.marvelapi.data.remote.api

import com.example.marvelapi.data.remote.CharacterResponse
import com.example.marvelapi.utils.ApiHash
import com.example.marvelapi.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 *
 *
 *
 * created on 23/11/2022
 * @author Lucas Goncalves
 */
interface MarvelService {

    @GET("characters")
    suspend fun getAllCharacters(
        @Query("ts") ts: Number = Constants.timeStamp,
        @Query("apikey") apiKey: String = Constants.PUBLIC_KEY,
        @Query("hash") hash: String = ApiHash.buildAndGetHash(),
        @Query("offset") offset: Int? = 0,
        @Query("limit") limit: Int? = 50,
    ): CharacterResponse

    @GET("characters")
    suspend fun getCharacterByName(
        @Query("nameStartsWith") search: String,
        @Query("ts") ts: Number = Constants.timeStamp,
        @Query("apikey") apiKey: String = Constants.PUBLIC_KEY,
        @Query("hash") hash: String = ApiHash.buildAndGetHash(),
        @Query("offset") offset: Int? = 0,
        @Query("limit") limit: Int? = 50,
    ): CharacterResponse

    @GET("characters/{characterId}")
    suspend fun getCharacterById(
        @Path("characterId") characterId: String,
        @Query("ts") ts: Number = Constants.timeStamp,
        @Query("apikey") apiKey: String = Constants.PUBLIC_KEY,
        @Query("hash") hash: String = ApiHash.buildAndGetHash(),
        @Query("offset") offset: Int? = 0,
        @Query("limit") limit: Int? = 50,
    ): CharacterResponse

    companion object {

        fun create(): MarvelService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MarvelService::class.java)
        }
    }
}