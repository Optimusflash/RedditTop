package com.optimus.reddittop.di

import com.optimus.reddittop.ui.main.MainActivity
import com.optimus.reddittop.di.modules.RemoteModule
import com.optimus.reddittop.di.modules.ViewModelModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Dmitriy Chebotar on 18.09.2020.
 */
@Singleton
@Component(modules = [RemoteModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}