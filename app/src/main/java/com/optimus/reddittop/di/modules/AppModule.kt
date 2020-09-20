package com.optimus.reddittop.di.modules

import android.app.Application
import com.optimus.reddittop.utils.ImageDownloader
import dagger.Module

import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Dmitriy Chebotar on 20.09.2020.
 */
@Module
class AppModule(val app: Application) {
    @Provides
    @Singleton
    fun provideApp() = app

    @Provides
    @Singleton
    fun provideImageDownloader() = ImageDownloader(app)
}