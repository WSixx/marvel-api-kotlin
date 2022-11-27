package com.example.marvelapi.data

import com.example.marvelapi.data.remote.Thumbnail


/**
 *
 *
 *
 * created on 25/11/2022
 * @author Lucas Goncalves
 */
interface CharacterInterfaceBase :java.io.Serializable {
    val id: Long
    val name: String
    val description: String
    val thumbnail: Thumbnail
    val imagePath: String?
}

abstract class AbstractCharacterInterface : CharacterInterfaceBase {
    override val thumbnail: Thumbnail
        get() = Thumbnail("", imagePath)
}