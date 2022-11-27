package com.example.marvelapi.data.remote

import android.text.TextUtils
import com.example.marvelapi.data.AbstractCharacterInterface

/**
 *
 *
 *
 * created on 23/11/2022
 * @author Lucas Goncalves
 */
data class CharacterInterfaceResult (
    override val id: Long,
    override val name: String,
    override val description: String,
    val modified: String,
    override val thumbnail: Thumbnail,
    override val imagePath: String? = null,
) : AbstractCharacterInterface(), java.io.Serializable

data class Thumbnail(
    val extension: String,
    val path: String?,
) {
    fun buildImage(): String {
        return if (!TextUtils.isEmpty(extension)) (this.path + "." + this.extension) else this.path!!
    }
}