package com.optimus.reddittop.ui.main

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.optimus.reddittop.data.model.RedditItem
import com.optimus.reddittop.data.paging.RedditDataSource
import com.optimus.reddittop.data.paging.RedditDataSourceFactory
import com.optimus.reddittop.data.repositories.RedditRepository
import com.optimus.reddittop.utils.State
import javax.inject.Inject

/**
 * Created by Dmitriy Chebotar on 18.09.2020.
 */
class MainViewModel @Inject constructor(private val repository: RedditRepository) : ViewModel() {
    private lateinit var _redditItemPageList: LiveData<PagedList<RedditItem>>
    val redditPublicationPageList: LiveData<PagedList<RedditItem>>
        get() = _redditItemPageList
    private lateinit var _redditDataSource: MutableLiveData<RedditDataSource>
    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String>
        get() = _imageUrl

    init {
        setupPagedListAndDataSource()
    }

    private fun setupPagedListAndDataSource() {
        val redditDataSourceFactory = RedditDataSourceFactory(viewModelScope)
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()
        _redditItemPageList = LivePagedListBuilder(redditDataSourceFactory, config).build()
        _redditDataSource = redditDataSourceFactory.redditLiveDataSource
    }

    fun getState(): LiveData<State> =
        Transformations.switchMap(_redditDataSource, RedditDataSource::state)

    fun retry() {
        _redditDataSource.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return _redditItemPageList.value?.isEmpty() ?: true
    }

    fun handleRvItemClick(imageUrl: String){
        _imageUrl.value = imageUrl
    }
}