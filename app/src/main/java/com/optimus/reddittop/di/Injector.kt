package com.optimus.reddittop.di

import android.app.Application
import android.content.Context
import com.optimus.reddittop.di.modules.AppModule
import com.optimus.reddittop.di.modules.RemoteModule

/**
 * Created by Dmitriy Chebotar on 18.09.2020.
 */

object Injector {
        private lateinit var appComponent: AppComponent

        fun createDaggerComponent(app: Application){
            appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(app))
                .remoteModule(RemoteModule())
                .build()
        }

        fun getAppComponent() = appComponent
}