package com.optimus.reddittop.ui.details

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.media.Image
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.optimus.reddittop.utils.ImageDownloader
import com.optimus.reddittop.utils.State
import javax.inject.Inject

/**
 * Created by Dmitriy Chebotar on 19.09.2020.
 */
class DetailsViewModel @Inject constructor(val app: Application, private val imageDownloader: ImageDownloader) : AndroidViewModel(app) {
    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String>
        get() = _imageUrl

    private val _downloadImageState = MutableLiveData<State>()
    val downloadImageState: LiveData<State>
        get() = _downloadImageState

    fun handleImageUrl(imageUrl: String){
        _imageUrl.value = imageUrl
    }

    fun loadImageToGallery(bitmap: Bitmap?) {
        if (bitmap==null) {
            _downloadImageState.value = State.ERROR
            return
        }
        imageDownloader.saveImage(bitmap){
            _downloadImageState.postValue(it)
        }
    }
}