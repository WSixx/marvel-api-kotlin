package com.example.marvelapi.data.repository

import androidx.annotation.WorkerThread
import com.example.marvelapi.data.database.FavoriteCharacterInterface
import com.example.marvelapi.data.database.FavoriteCharacterDao
import com.example.marvelapi.utils.Resource
import com.example.marvelapi.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 *
 *
 *
 * created on 25/11/2022
 * @author Lucas Goncalves
 */
class LocalFavoritesCharactersRepository(private val favoriteCharacterDao: FavoriteCharacterDao) {

    val allFavoritesCharacters: Flow<List<FavoriteCharacterInterface>> = favoriteCharacterDao.getAll()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @WorkerThread
    suspend fun insert(favCharacter: FavoriteCharacterInterface) {
        favoriteCharacterDao.insert(favCharacter)
    }

    @WorkerThread
    suspend fun delete(favCharacter: FavoriteCharacterInterface) {
        favoriteCharacterDao.delete(favCharacter)
    }

    suspend fun getById(id: String): Resource<FavoriteCharacterInterface> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val results = favoriteCharacterDao.getById(id)
                Resource.Success(results)
            }
        }
    }

}