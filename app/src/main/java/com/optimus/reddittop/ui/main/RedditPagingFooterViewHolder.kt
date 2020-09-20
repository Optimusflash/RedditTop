package com.optimus.reddittop.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.optimus.reddittop.databinding.RvRedditPagingFooterBinding
import com.optimus.reddittop.utils.State


/**
 * Created by Dmitriy Chebotar on 18.09.2020.
 */
class RedditPagingFooterViewHolder(
    private val binding: RvRedditPagingFooterBinding,
    private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup, onPostClick: () -> Unit): RedditPagingFooterViewHolder {
            val rvRedditPagingFooterBinding = RvRedditPagingFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return RedditPagingFooterViewHolder(rvRedditPagingFooterBinding, onPostClick)
        }
    }

    fun bind (state: State){
        binding.progressBar.visibility = if (state == State.LOADING) View.VISIBLE else View.INVISIBLE
        binding.btnRetry.visibility = if (state == State.ERROR) View.VISIBLE else View.INVISIBLE
        binding.tvError.visibility = if (state == State.ERROR) View.VISIBLE else View.INVISIBLE
        binding.btnRetry.setOnClickListener { retryCallback.invoke()  }
    }
}