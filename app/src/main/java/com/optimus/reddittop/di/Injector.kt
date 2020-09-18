package com.optimus.reddittop.di

/**
 * Created by Dmitriy Chebotar on 18.09.2020.
 */

class Injector {
    companion object{
        private lateinit var appComponent: AppComponent

        fun createDaggerComponent(){
            appComponent = DaggerAppComponent.create()
        }

        fun getAppComponent() = appComponent
    }
}