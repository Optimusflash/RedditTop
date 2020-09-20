package com.optimus.reddittop.ui.main

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.optimus.reddittop.data.model.RedditItem
import com.optimus.reddittop.utils.State

/**
 * Created by Dmitriy Chebotar on 18.09.2020.
 */

private const val DATA_VIEW_TYPE = 1
private const val FOOTER_VIEW_TYPE = 2
class RedditPagingAdapter (private val onItemClick: (id: String?)->Unit, private val onRetryClick: ()->Unit): PagedListAdapter<RedditItem, RecyclerView.ViewHolder>(REDDIT_COMPARATOR)  {

    private var state = State.LOADING

    companion object {
        private val REDDIT_COMPARATOR = object : DiffUtil.ItemCallback<RedditItem>() {
            override fun areItemsTheSame(oldItem: RedditItem, newItem: RedditItem): Boolean {
                return oldItem.redditPublication.id == newItem.redditPublication.id
            }

            override fun areContentsTheSame(oldItem: RedditItem, newItem: RedditItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            DATA_VIEW_TYPE -> {
                RedditPostViewHolder.create(parent, onItemClick)
            }
            else -> {
                RedditPagingFooterViewHolder.create(parent, onRetryClick)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            DATA_VIEW_TYPE -> {
                getItem(position)?.let {
                    (holder as RedditPostViewHolder).bind(it)
                }
            }
            FOOTER_VIEW_TYPE -> {
                (holder as RedditPagingFooterViewHolder).bind(state)
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }
}