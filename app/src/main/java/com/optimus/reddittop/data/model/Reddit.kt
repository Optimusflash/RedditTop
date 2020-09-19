package com.optimus.reddittop.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Dmitriy Chebotar on 18.09.2020.
 */

data class RedditResponse(
    @SerializedName("data")
    val redditData: RedditData
)

data class RedditData(
    @SerializedName("children")
    val redditItems: List<RedditItem>,
    @SerializedName("after")
    val pagingToken: String
)

data class RedditItem(
    val kind: String,
    @SerializedName("data")
    val redditPublication: RedditPublication
)

data class RedditPublication(
    val id: String,
    val author: String?,
    val thumbnail: String?,
    @SerializedName("num_comments")
    val commentsCount: Int?,
    @SerializedName("created_utc")
    val createdDate: Double?,
    val preview: Preview
)

data class Preview(
    val images: List<Image>
)

data class Image(
    @SerializedName("source")
    val sourceImage: SourceImage
)

data class SourceImage(
    val url: String,
    val width : Int,
    val height: Int,
)