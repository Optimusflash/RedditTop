package com.optimus.reddittop.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.optimus.reddittop.R

/**
 * Created by Dmitriy Chebotar on 18.09.2020.
 */

fun ImageView.loadImage(url: String?){
    Glide.with(context)
        .load(url)
        .error(R.drawable.no_image_found)
        .into(this)
}
