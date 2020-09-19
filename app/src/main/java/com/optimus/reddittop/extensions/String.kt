package com.optimus.reddittop.extensions

/**
 * Created by Dmitriy Chebotar on 19.09.2020.
 */

fun String.toCorrectUrl(): String = this.replace(Regex("&amp;"), "&")