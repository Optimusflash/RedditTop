package com.optimus.reddittop.data.repositories

import com.optimus.reddittop.data.remote.RedditService
import javax.inject.Inject

/**
 * Created by Dmitriy Chebotar on 18.09.2020.
 */
class RedditRepository @Inject constructor(private val api: RedditService) {
    suspend fun loadRedditPublications() = api.getTopRedditPublication()
}