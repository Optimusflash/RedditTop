package com.optimus.reddittop.ui.details


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        private const val PERMISSION_CODE = 1001
        private const val EXTRA_IMAGE_URL = "extra_image_url"
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

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.static_animation, R.anim.zoom_out)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    val bitmap = (binding.ivDetails.drawable as? BitmapDrawable)?.bitmap
                    detailsViewModel.loadImageToGallery(bitmap)
                } else {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.permission_request_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun initDaggerComponent() {
        Injector.getAppComponent().inject(this)
    }

    private fun initViewModels() {
        detailsViewModel =
            ViewModelProvider(this, viewModelFactory).get(DetailsViewModel::class.java)
    }

    private fun initViews() {
        binding.ivBtnDownload.setOnClickListener { checkPermissions() }
        binding.ivBtnBack.setOnClickListener { onBackPressed() }
    }

    private fun setObservers() {
        intent.getStringExtra(EXTRA_IMAGE_URL)?.let(detailsViewModel::handleImageUrl)
        detailsViewModel.imageUrl.observe(this, {
            binding.ivDetails.loadImage(it) { isReady ->
                binding.progressBarDetailsImage.visibility =
                    if (isReady) View.GONE else View.VISIBLE
            }
        })
        detailsViewModel.downloadImageState.observe(this, {
            updateUi(it)
        })
        detailsViewModel.permissionState.observe(this, { permissionState ->
            if (permissionState.not()) {
                requestPermission()
            } else {
                val bitmap = (binding.ivDetails.drawable as? BitmapDrawable)?.bitmap
                detailsViewModel.loadImageToGallery(bitmap)
            }
        })
    }

    private fun updateUi(it: State?) {
        it ?: return
        when (it) {
            State.LOADING -> Toast.makeText(
                this,
                resources.getString(R.string.toast_loading_message),
                Toast.LENGTH_SHORT
            ).show()
            State.DONE -> Toast.makeText(
                this,
                resources.getString(R.string.toast_done_message),
                Toast.LENGTH_SHORT
            ).show()
            else -> Toast.makeText(
                this,
                resources.getString(R.string.toast_error_message),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkPermissions() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            detailsViewModel.setPermissionState(false)
        } else {
            detailsViewModel.setPermissionState(true)
        }
    }

    private fun requestPermission() {
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        requestPermissions(permissions, PERMISSION_CODE)
    }
}