package com.example.marvelapi.data.repository

import com.example.marvelapi.data.remote.api.MarvelService
import com.example.marvelapi.data.remote.CharacterInterfaceResult
import com.example.marvelapi.utils.Resource
import com.example.marvelapi.utils.safeCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *
 *
 *
 * created on 23/11/2022
 * @author Lucas Goncalves
 */
class AllCharactersRepository(private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default) {

    suspend fun getAllCharacters(): Resource<List<CharacterInterfaceResult>> {
        return withContext(defaultDispatcher) {
            safeCall {
                val results = MarvelService.create().getAllCharacters().data.characterResults
                Resource.Success(results)
            }
        }
    }

    suspend fun getCharacterByName(name: String): Resource<List<CharacterInterfaceResult>> {
        return withContext(defaultDispatcher) {
            safeCall {
                val results = MarvelService.create().getCharacterByName(name).data.characterResults
                Resource.Success(results)
            }
        }
    }

    suspend fun getCharacterById(id: String): Resource<CharacterInterfaceResult> {
        return withContext(defaultDispatcher) {
            safeCall {
                val results = MarvelService.create().getCharacterById(id).data.characterResults[0]
                Resource.Success(results)
            }
        }
    }
}