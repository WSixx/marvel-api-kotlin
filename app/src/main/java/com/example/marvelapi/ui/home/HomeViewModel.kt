package com.example.marvelapi.ui.home

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.marvelapi.MarvelApiApplication
import com.example.marvelapi.data.database.FavoriteCharacterInterface
import com.example.marvelapi.data.remote.CharacterInterfaceResult
import com.example.marvelapi.data.repository.AllCharactersRepository
import com.example.marvelapi.data.repository.LocalFavoritesCharactersRepository
import com.example.marvelapi.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class HomeViewModel(
    private val localRepository: LocalFavoritesCharactersRepository,
) : ViewModel() {

    private val _getAllCharactersStatus = MutableStateFlow<Resource<List<CharacterInterfaceResult>>>(Resource.Loading())
    val allCharactersStatus: StateFlow<Resource<List<CharacterInterfaceResult>>> = _getAllCharactersStatus

    private val _getFromDatabaseById = MutableStateFlow<Resource<FavoriteCharacterInterface>>(Resource.Loading())
    val getFromDatabaseById: StateFlow<Resource<FavoriteCharacterInterface>> = _getFromDatabaseById

    private val _getAllCharactersContains = MutableStateFlow(false)
    val getAllCharactersContains: StateFlow<Boolean> = _getAllCharactersContains


    private val _getSingleCharactersStatus = MutableStateFlow<Resource<CharacterInterfaceResult>>(Resource.Loading())
    val getSingleCharactersStatus: StateFlow<Resource<CharacterInterfaceResult>> = _getSingleCharactersStatus

    val allFavorites: LiveData<List<FavoriteCharacterInterface>> =
        localRepository.allFavoritesCharacters.asLiveData()

    private val repository = AllCharactersRepository(Dispatchers.IO)

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val localRepository = (this[APPLICATION_KEY] as MarvelApiApplication).repository
                HomeViewModel(
                    localRepository = localRepository,
                )
            }
        }
    }

    fun insert(favorite: FavoriteCharacterInterface) = viewModelScope.launch(Dispatchers.Main) {
        localRepository.insert(favorite)
    }

    fun deleteFav(favorite: FavoriteCharacterInterface) = viewModelScope.launch(Dispatchers.Main) {
        localRepository.delete(favorite)
    }

    fun getById(id: String) = viewModelScope.launch(Dispatchers.Main) {
        localRepository.getById(id)
    }

    fun getMarvelCharacters() {
        viewModelScope.launch(Dispatchers.Main) {
            val callResult = repository.getAllCharacters()
            _getAllCharactersStatus.value = callResult
        }
    }

    fun getMarvelCharactersByName(name: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val callResult = repository.getCharacterByName(name)
            _getAllCharactersStatus.value = callResult
        }
    }

    fun getMarvelCharactersById(id: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val callResult = repository.getCharacterById(id)
            _getSingleCharactersStatus.value = callResult
        }
    }

    fun getMarvelCharactersByIdDatabase(id: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val callResult = localRepository.getById(id)
            _getFromDatabaseById.value = callResult
        }
    }
}