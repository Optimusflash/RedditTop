package com.optimus.reddittop.ui.details

import android.media.Image
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

/**
 * Created by Dmitriy Chebotar on 19.09.2020.
 */
class DetailsViewModel @Inject constructor() : ViewModel() {
    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String>
        get() = _imageUrl

    fun handleImageUrl(imageUrl: String){
        _imageUrl.value = imageUrl
    }
}