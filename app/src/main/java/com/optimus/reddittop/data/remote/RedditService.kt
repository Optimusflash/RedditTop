package com.optimus.reddittop.data.remote

import com.optimus.reddittop.data.model.RedditResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Dmitriy Chebotar on 18.09.2020.
 */
interface RedditService {
    @GET("top.json")
    suspend fun getTopRedditPublication(@Query("after")pagingToken: String? = null): RedditResponse
}