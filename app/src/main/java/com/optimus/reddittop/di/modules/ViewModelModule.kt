package com.optimus.reddittop.di.modules

import androidx.lifecycle.ViewModel
import com.optimus.reddittop.di.ViewModelKey
import com.optimus.reddittop.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Dmitriy Chebotar on 18.09.2020.
 */
@Module
abstract class ViewModelModule {
    @IntoMap
    @Binds
    @ViewModelKey(MainViewModel::class)
    abstract fun provideMainViewModel(mainViewModel: MainViewModel): ViewModel
}