package com.optimus.reddittop.ui.details

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.optimus.reddittop.R
import com.optimus.reddittop.databinding.ActivityDetailBinding


import com.optimus.reddittop.di.Injector
import com.optimus.reddittop.di.ViewModelFactory
import com.optimus.reddittop.extensions.loadImage
import java.io.IOException
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
        ActivityCompat.requestPermissions(this,  arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1);
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
        binding.ivBtnDownload.setOnClickListener { saveImage() }
        binding.ivBtnBack.setOnClickListener { onBackPressed() }
    }

    private fun saveImage() {
        Toast.makeText(this, "Saving image. Please wait...", Toast.LENGTH_SHORT).show()
        val ivDetails = binding.ivDetails
        val bitmap = (ivDetails.drawable as BitmapDrawable).bitmap
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis().toString())
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val imageUri = applicationContext.contentResolver.insert(collection, values)

        try {
            val stream = this.contentResolver.openOutputStream(imageUri!!)
            stream?.let {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
            stream?.flush()
            stream?.close()
            Toast.makeText(this, "Image Saved Successfully", Toast.LENGTH_SHORT).show()

        } catch (e: IOException) {
            Toast.makeText(this, "Failed To Save Image. Please Try Again", Toast.LENGTH_SHORT).show()
        }

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