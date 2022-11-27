package com.example.marvelapi.ui.details


import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.marvelapi.MarvelApiApplication
import com.example.marvelapi.data.repository.AllCharactersRepository
import com.example.marvelapi.data.repository.LocalFavoritesCharactersRepository
import com.example.marvelapi.ui.home.HomeViewModel

class DetailsViewModel(
    private val localRepository: LocalFavoritesCharactersRepository,
    private val allCharactersRepository: AllCharactersRepository,
) : HomeViewModel(localRepository) {

    // Define ViewModel factory in a companion object
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val localRepository =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MarvelApiApplication).repository
                val allCharactersRepository =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MarvelApiApplication).repositoryApi
                DetailsViewModel(
                    localRepository = localRepository,
                    allCharactersRepository = allCharactersRepository
                )
            }
        }
    }

}