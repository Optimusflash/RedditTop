package com.optimus.reddittop.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * Created by Dmitriy Chebotar on 20.09.2020.
 */
class ImageDownloader(private val context: Context) {

    fun saveImage(bitmap: Bitmap, state: (State)-> Unit) {
        GlobalScope.launch(Dispatchers.IO){
            try {
                state.invoke(State.LOADING)
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis().toString())
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                }
                val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                context.contentResolver.insert(collection, values)?.let { imageUri->
                    try {
                        val stream = context.contentResolver.openOutputStream(imageUri)
                        stream?.let {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                        }
                        stream?.flush()
                        stream?.close()
                        state.invoke(State.DONE)
                    } catch (e: IOException) {
                        state.invoke(State.ERROR)
                    }
                }
            } catch (e: Exception){
                state.invoke(State.ERROR)
            }
        }
    }
}