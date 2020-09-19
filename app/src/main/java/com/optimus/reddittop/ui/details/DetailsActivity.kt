package com.optimus.reddittop.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.optimus.reddittop.R
import com.optimus.reddittop.databinding.ActivityDetailBinding
import com.optimus.reddittop.di.Injector
import com.optimus.reddittop.di.ViewModelFactory
import com.optimus.reddittop.extensions.loadImage
import javax.inject.Inject

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var detailsViewModel: DetailsViewModel

    companion object {
        const val EXTRA_IMAGE_URL = "extra_image_url"
        fun newIntent(context: Context, imageUrl: String) =
            Intent(context, DetailsActivity::class.java).apply {
                putExtra(EXTRA_IMAGE_URL, imageUrl)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
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
        detailsViewModel =
            ViewModelProvider(this, viewModelFactory).get(DetailsViewModel::class.java)
    }

    private fun initViews() {
        //TODO
    }

    private fun setObservers() {
        intent.getStringExtra(EXTRA_IMAGE_URL)?.let(detailsViewModel::handleImageUrl)
        detailsViewModel.imageUrl.observe(this, {
            binding.ivDetails.loadImage(it)

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.static_animation, R.anim.zoom_out)
    }
}