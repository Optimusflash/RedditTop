package com.optimus.reddittop.data.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.optimus.reddittop.data.model.RedditItem
import com.optimus.reddittop.data.remote.RedditService
import com.optimus.reddittop.di.Injector
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**
 * Created by Dmitriy Chebotar on 18.09.2020.
 */
class RedditDataSourceFactory(private val scope: CoroutineScope): DataSource.Factory<String, RedditItem>() {

    @Inject
    lateinit var redditService: RedditService
    private val _redditLiveDataSource = MutableLiveData<RedditDataSource>()
    val redditLiveDataSource: MutableLiveData<RedditDataSource>
        get() = _redditLiveDataSource

    init {
        Injector.getAppComponent().inject(this)
    }

    override fun create(): DataSource<String, RedditItem> {
        val redditDataSource = RedditDataSource(redditService, scope)
        _redditLiveDataSource.postValue(redditDataSource)
        return redditDataSource
    }
}