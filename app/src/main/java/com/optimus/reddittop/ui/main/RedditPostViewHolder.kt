package com.optimus.reddittop.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.optimus.reddittop.data.model.RedditItem
import com.optimus.reddittop.databinding.RvRedditPostSellBinding
import com.optimus.reddittop.extensions.loadImage
import com.optimus.reddittop.extensions.toCorrectUrl
import com.optimus.reddittop.extensions.toRelativeDateFormat


/**
 * Created by Dmitriy Chebotar on 18.09.2020.
 */
class RedditPostViewHolder(
    private val binding: RvRedditPostSellBinding,
    private val onPostClick: (id: String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(parent: ViewGroup, onPostClick: (id: String) -> Unit): RedditPostViewHolder {
            val rvRedditPostSellBinding = RvRedditPostSellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return RedditPostViewHolder(rvRedditPostSellBinding, onPostClick)
        }
    }

    fun bind (redditItem: RedditItem){
        binding.tvAuthorName.text = redditItem.redditPublication.author
        binding.tvDateCreated.text = redditItem.redditPublication.createdDate?.toRelativeDateFormat()
        binding.ivRedditPostImage.loadImage(redditItem.redditPublication.thumbnail)
        binding.tvCommentsCount.text = redditItem.redditPublication.commentsCount.toString()
        binding.root.setOnClickListener { onPostClick.invoke(redditItem.redditPublication.preview.images[0].sourceImage.url.toCorrectUrl())}
    }
}