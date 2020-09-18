package com.optimus.reddittop.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Created by Dmitriy Chebotar on 18.09.2020.
 */
@Singleton
class ViewModelFactory @Inject constructor(private val viewModelMap: MutableMap<Class<out ViewModel>, Provider<ViewModel>>): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val provider = viewModelMap[modelClass] ?: throw IllegalArgumentException("Object ${modelClass.simpleName} cannot be created")
        return provider.get() as T
    }
}