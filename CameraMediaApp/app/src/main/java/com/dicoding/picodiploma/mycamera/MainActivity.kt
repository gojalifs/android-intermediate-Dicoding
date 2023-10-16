package com.dicoding.picodiploma.mycamera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.dicoding.picodiploma.mycamera.CameraActivity.Companion.CAMERAX_RESULT
import com.dicoding.picodiploma.mycamera.data.ResultState
import com.dicoding.picodiploma.mycamera.data.factory.ViewModelFactory
import com.dicoding.picodiploma.mycamera.data.view_model.MainViewModel
import com.dicoding.picodiploma.mycamera.databinding.ActivityMainBinding
import com.dicoding.picodiploma.mycamera.utils.Utils.getFileFromUri
import com.dicoding.picodiploma.mycamera.utils.Utils.getImageUri
import com.dicoding.picodiploma.mycamera.utils.Utils.reduceFileSize

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentImageUri: Uri? = null
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!grantPermission()) {
            requestPermissionLauncher.launch(MainActivity.REQUIRED_PERMISSION)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.cameraXButton.setOnClickListener { startCameraX() }
        binding.uploadButton.setOnClickListener { uploadImage() }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Toast.makeText(this, "No media selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Toast.makeText(this, "Image Picked", Toast.LENGTH_SHORT).show()
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launchIntentCamera.launch(currentImageUri)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
        }
    }

    private fun grantPermission() = ContextCompat.checkSelfPermission(
        this,
        MainActivity.REQUIRED_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED

    private val launchIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun startCameraX() {

        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun uploadImage() {
        currentImageUri?.let {
            val imageFile = getFileFromUri(it, this).reduceFileSize()
            Toast.makeText(this, "image : ${imageFile.path}", Toast.LENGTH_SHORT).show()
            val description = "Ini deskripsi"

            viewModel.uploadImage(imageFile, description).observe(this) { state ->
                if (state != null) {
                    when (state) {
                        is ResultState.Loading -> showLoading(true)
                        is ResultState.Success -> {
                            showToast(state.data.message)
                            showLoading(false)
                        }

                        is ResultState.Error -> {
                            showToast(state.error)
                            showLoading(false)
                        }
                    }
                }
            }

        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
