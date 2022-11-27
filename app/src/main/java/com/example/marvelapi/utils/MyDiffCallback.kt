package com.example.marvelapi.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.marvelapi.data.CharacterInterfaceBase

/**
 *
 *
 *
 * created on 25/11/2022
 * @author Lucas Goncalves
 */

class MyDiffCallback<T : CharacterInterfaceBase> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return ((oldItem.name == newItem.name) &&
                (oldItem.description == newItem.description))    }
}