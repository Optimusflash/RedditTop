package com.optimus.reddittop.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.optimus.reddittop.R
import com.optimus.reddittop.di.Injector
import com.optimus.reddittop.di.ViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDaggerComponent()
        initViewModels()
        initViews()
        setObservers()
    }

    private fun initDaggerComponent() {
        Injector.getAppComponent().inject(this)
    }

    private fun initViewModels() {
        mainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private fun initViews() {

    }

    private fun setObservers() {
        mainViewModel.redditResponse.observe(this, {
            Log.e("M_MainActivity", "${it.redditData.redditItems.map {redditItem ->  
                redditItem.redditPublication.author
            }}")
        })
    }
}