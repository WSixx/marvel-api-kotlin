package com.example.marvelapi.ui.home

import android.widget.ToggleButton
import com.example.marvelapi.data.CharacterInterfaceBase

/**
 *
 *
 *
 * created on 24/11/2022
 * @author Lucas Goncalves
 */
interface OnFavoriteClickListener {

    /**
     * Handles clicks events on Favorite Button
     */
    fun onClick(view: ToggleButton, data: CharacterInterfaceBase) {}
}