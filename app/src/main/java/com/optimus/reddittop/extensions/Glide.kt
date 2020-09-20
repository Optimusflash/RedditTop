package com.optimus.reddittop.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.optimus.reddittop.R

/**
 * Created by Dmitriy Chebotar on 18.09.2020.
 */

fun ImageView.loadImage(url: String?, isImageReady: ((Boolean)->Unit)? = null){
    Glide.with(context)
        .load(url)
        .listener(object : RequestListener<Drawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                isImageReady?.invoke(false)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                isImageReady?.invoke(true)
                return false
            }
        })
        .fitCenter()
        .error(R.drawable.no_image_found)
        .into(this)
}
