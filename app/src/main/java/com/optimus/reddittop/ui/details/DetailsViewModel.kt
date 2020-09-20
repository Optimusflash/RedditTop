package com.optimus.reddittop.ui.details

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.optimus.reddittop.utils.ImageDownloader
import com.optimus.reddittop.utils.SingleLiveEvent
import com.optimus.reddittop.utils.State
import javax.inject.Inject

/**
 * Created by Dmitriy Chebotar on 19.09.2020.
 */
class DetailsViewModel @Inject constructor(val app: Application, private val imageDownloader: ImageDownloader) : AndroidViewModel(app) {
    private val _imageUrl = SingleLiveEvent<String>()
    val imageUrl: LiveData<String>
        get() = _imageUrl

    private val _downloadImageState = SingleLiveEvent<State>()
    val downloadImageState: LiveData<State>
        get() = _downloadImageState

    private val _permissionState = SingleLiveEvent<Boolean>()
    val permissionState: LiveData<Boolean>
        get() = _permissionState

    fun handleImageUrl(imageUrl: String) {
        _imageUrl.value = imageUrl
    }

    fun loadImageToGallery(bitmap: Bitmap?) {
        if (bitmap == null) {
            _downloadImageState.value = State.ERROR
            return
        }
        imageDownloader.saveImage(bitmap){
            _downloadImageState.postValue(it)
        }
    }

    fun setPermissionState(permissionState: Boolean ){
        _permissionState.value = permissionState
    }
}