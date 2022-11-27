package com.example.marvelapi.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 *
 *
 *
 * created on 25/11/2022
 * @author Lucas Goncalves
 */
@Dao
interface FavoriteCharacterDao {

    @Query("SELECT * FROM FavoriteCharacterInterface")
    fun getAll(): Flow<List<FavoriteCharacterInterface>>

    @Query("SELECT * FROM FavoriteCharacterInterface WHERE FavoriteCharacterInterface.id = :id ")
    fun getById(id: String): FavoriteCharacterInterface

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favCharacter: FavoriteCharacterInterface)

    @Delete
    suspend fun delete(favCharacter: FavoriteCharacterInterface)
}
