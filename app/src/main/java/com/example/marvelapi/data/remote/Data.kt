package com.example.marvelapi.data.remote

import com.google.gson.annotations.SerializedName

/**
 *
 *
 *
 * created on 23/11/2022
 * @author Lucas Goncalves
 */
data class Data(
    val offset: Long,
    val limit: Long,
    @field:SerializedName("total")
    val total: Long,
    val count: Long,
    @field:SerializedName("results")
    val characterResults: List<CharacterInterfaceResult>,
)