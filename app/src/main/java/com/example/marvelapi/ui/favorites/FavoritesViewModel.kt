package com.example.marvelapi.ui.favorites

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.marvelapi.MarvelApiApplication
import com.example.marvelapi.data.repository.LocalFavoritesCharactersRepository
import com.example.marvelapi.ui.home.HomeViewModel

class FavoritesViewModel(
    private val localRepository: LocalFavoritesCharactersRepository,
) : HomeViewModel(localRepository) {

    // Define ViewModel factory in a companion object
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val localRepository =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MarvelApiApplication).repository
                FavoritesViewModel(
                    localRepository = localRepository,
                )
            }
        }
    }

}