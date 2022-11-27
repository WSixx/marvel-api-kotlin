package com.example.marvelapi

import android.app.Application
import com.example.marvelapi.data.database.AppDatabase
import com.example.marvelapi.data.repository.AllCharactersRepository
import com.example.marvelapi.data.repository.LocalFavoritesCharactersRepository
import com.example.marvelapi.helpers.network.NetworkStatusRepository
import kotlinx.coroutines.MainScope

/**
 *
 * My Base class for maintaining global application state
 *
 * created on 25/11/2022
 * @author Lucas Goncalves
 */
class MarvelApiApplication: Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { LocalFavoritesCharactersRepository(database.favoriteCharacterDao()) }
    val repositoryApi by lazy { AllCharactersRepository() }
    val networkRepository by lazy { NetworkStatusRepository(context = this, appScope = MainScope()) }
}