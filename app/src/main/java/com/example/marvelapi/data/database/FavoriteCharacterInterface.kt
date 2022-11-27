package com.example.marvelapi.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.marvelapi.data.AbstractCharacterInterface

/**
 *
 *
 *
 * created on 25/11/2022
 * @author Lucas Goncalves
 */
@Entity
data class FavoriteCharacterInterface(
    @PrimaryKey override val id: Long,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "description") override val description: String,
    @ColumnInfo(name = "image_path") override val imagePath: String?,
) : AbstractCharacterInterface()