package com.example.marvelapi.data.remote

import com.google.gson.annotations.SerializedName

/**
 *
 *
 *
 * created on 23/11/2022
 * @author Lucas Goncalves
 */
data class CharacterResponse(@field:SerializedName("data") val data: Data)