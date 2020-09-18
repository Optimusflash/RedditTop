package com.optimus.reddittop.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.optimus.reddittop.data.model.RedditResponse
import com.optimus.reddittop.data.repositories.RedditRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Dmitriy Chebotar on 18.09.2020.
 */
class MainViewModel @Inject constructor(private val repository: RedditRepository) : ViewModel() {

    private lateinit var _redditResponse: LiveData<RedditResponse>
    val redditResponse: LiveData<RedditResponse>
        get() = _redditResponse

    init {
        viewModelScope.launch {
            _redditResponse = liveData {
                emit(repository.loadRedditPublications())
            }
        }
    }

}