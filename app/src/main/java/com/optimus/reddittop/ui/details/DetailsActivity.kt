package com.optimus.reddittop.ui.details


import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.optimus.reddittop.R
import com.optimus.reddittop.databinding.ActivityDetailBinding
import com.optimus.reddittop.di.Injector
import com.optimus.reddittop.di.ViewModelFactory
import com.optimus.reddittop.extensions.loadImage
import com.optimus.reddittop.utils.State
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
        ActivityCompat.requestPermissions(this,  arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1); //TODO
        ActivityCompat.requestPermissions(this,  arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1);
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
        binding.ivBtnDownload.setOnClickListener {
            val bitmap = (binding.ivDetails.drawable as? BitmapDrawable)?.bitmap
            detailsViewModel.loadImageToGallery(bitmap)
        }
        binding.ivBtnBack.setOnClickListener { onBackPressed() }
    }

      private fun setObservers() {
        intent.getStringExtra(EXTRA_IMAGE_URL)?.let(detailsViewModel::handleImageUrl)
        detailsViewModel.imageUrl.observe(this, {
            binding.ivDetails.loadImage(it)
        })
        detailsViewModel.downloadImageState.observe(this, {
            updateUi(it)
        })
    }

    private fun updateUi(it: State?) {
        when(it){
            State.LOADING -> Toast.makeText(this, resources.getString(R.string.toast_loading_message), Toast.LENGTH_SHORT).show()
            State.DONE -> Toast.makeText(this, resources.getString(R.string.toast_done_message), Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this, resources.getString(R.string.toast_error_message), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.static_animation, R.anim.zoom_out)
    }
}