package com.example.marvelapi.utils

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.example.marvelapi.R
import com.squareup.picasso.Picasso

/**
 *
 *
 *
 * created on 26/11/2022
 * @author Lucas Goncalves
 */
object LoadImage {

    private const val TAG : String =  "LoadImage"

    fun loadImage(uri: Uri, into: ImageView) {
        try {
            Picasso.get()
                .load(uri)
                .error(R.drawable.ic_error_24)
                .fit()
                .into(into)
        } catch (e: java.lang.UnsupportedOperationException) {
            Log.e(TAG, e.message.toString())
        }

    }
}