package com.example.marvelapi.data.repository

import com.example.marvelapi.model.Characters

/**
 *
 *
 *
 * created on 23/11/2022
 * @author Lucas Goncalves
 */
interface ApiRepository {

    suspend fun getAllCharacters(): Characters
}