package com.optimus.reddittop.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.optimus.reddittop.R
import com.optimus.reddittop.databinding.ActivityMainBinding
import com.optimus.reddittop.di.Injector
import com.optimus.reddittop.di.ViewModelFactory
import com.optimus.reddittop.ui.details.DetailsActivity
import com.optimus.reddittop.ui.main.adapter.RedditPagingAdapter
import com.optimus.reddittop.utils.State
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mainViewModel: MainViewModel
    private val redditPagingAdapter by lazy {
        RedditPagingAdapter(mainViewModel::handleRvItemClick, mainViewModel::retry)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.mainRecyclerView.addItemDecoration(dividerItemDecoration)
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.mainRecyclerView.adapter = redditPagingAdapter
    }

    private fun setObservers() {
        mainViewModel.redditPublicationPageList.observe(this, {
            redditPagingAdapter.submitList(it)
        })
        mainViewModel.getState().observe(this, {
            if (mainViewModel.listIsEmpty().not()) {
                redditPagingAdapter.setState(it ?: State.DONE)
            }
            updateUi(it)
        })
        mainViewModel.imageUrl.observe(this, { imageUrl ->
            if (imageUrl == null) {
                Toast.makeText(this, resources.getString(R.string.cannot_open_image), Toast.LENGTH_SHORT).show()
                return@observe
            }
            startActivity(DetailsActivity.newIntent(this, imageUrl))
            overridePendingTransition(R.anim.zoom_in, R.anim.alpha_out)
        })
    }

    private fun updateUi(it: State?) {
        when (it) {
            State.LOADING -> {
                if (mainViewModel.listIsEmpty()) {
                    with(binding) {
                        progressBar.visibility = View.VISIBLE
                        mainRecyclerView.visibility = View.GONE
                        tvError.visibility = View.GONE
                    }
                }
            }
            State.ERROR -> {
                if (mainViewModel.listIsEmpty()) {
                    with(binding) {
                        progressBar.visibility = View.GONE
                        tvError.visibility = View.VISIBLE
                        tvError.setOnClickListener { mainViewModel.retry() }
                    }
                }
            }
            State.DONE -> {
                with(binding) {
                    progressBar.visibility = View.GONE
                    mainRecyclerView.visibility = View.VISIBLE
                    tvError.visibility = View.GONE
                }
            }
        }
    }
}