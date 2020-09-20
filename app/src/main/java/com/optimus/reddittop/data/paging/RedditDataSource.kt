package com.optimus.reddittop.data.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.optimus.reddittop.data.model.RedditItem
import com.optimus.reddittop.data.model.RedditResponse
import com.optimus.reddittop.data.remote.RedditService
import com.optimus.reddittop.utils.State
import com.optimus.reddittop.utils.Thumbnail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Dmitriy Chebotar on 18.09.2020.
 */
class RedditDataSource(private val redditService: RedditService, private val scope: CoroutineScope): PageKeyedDataSource<String, RedditItem>() {
    var state: MutableLiveData<State> = MutableLiveData()
    private lateinit var retryBlock: () -> Unit

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, RedditItem>
    ) {
        scope.launch {
            try {
                updateState(State.LOADING)
                val redditResponse = redditService.getTopRedditPublication()
                val pagingToken = redditResponse.redditData.pagingToken
                val filteredRedditItems = filterRedditItems(redditResponse)
                callback.onResult(filteredRedditItems, null, pagingToken)
                updateState(State.DONE)
            } catch (e: Exception){
                updateState(State.ERROR)
                retryBlock = { loadInitial(params, callback) }
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, RedditItem>
    ) {
        scope.launch {
            try {
                updateState(State.LOADING)
                val key = params.key
                val redditResponse = redditService.getTopRedditPublication(key)
                val pagingToken = redditResponse.redditData.pagingToken
                val filteredRedditItems = filterRedditItems(redditResponse)
                callback.onResult(filteredRedditItems, pagingToken)
                updateState(State.DONE)
            } catch (e: Exception){
                updateState(State.ERROR)
                retryBlock = { loadAfter(params, callback) }
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, RedditItem>
    ) {

    }

    private fun filterRedditItems(redditResponse: RedditResponse): List<RedditItem> {
        return redditResponse.redditData.redditItems.filterNot {
            it.redditPublication.thumbnail == Thumbnail.DEFAULT.value ||
                    it.redditPublication.thumbnail == Thumbnail.SELF.value ||
                    it.redditPublication.thumbnail == Thumbnail.IMAGE.value ||
                    it.redditPublication.thumbnail == Thumbnail.NSFW.value ||
                    it.redditPublication.thumbnail == Thumbnail.SPOILER.value
        }
    }

    fun retry() {
        CoroutineScope(Dispatchers.IO).launch {
            retryBlock.invoke()
        }
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }
}