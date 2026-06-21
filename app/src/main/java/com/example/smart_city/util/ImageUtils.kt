package com.example.smart_city.util

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class ImageUtils(private val activity: Activity, private val registryOwner: ActivityResultRegistryOwner) {
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraLauncher: ActivityResultLauncher<Void?>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var onImageSelectedCallback: ((Uri?) -> Unit)? = null

    fun registerLaunchers(onImageSelected: (Uri?) -> Unit) {
        onImageSelectedCallback = onImageSelected

        // Register for selecting image from gallery
        galleryLauncher = registryOwner.activityResultRegistry.register(
            "galleryLauncher", ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val uri = result.data?.data
            if (result.resultCode == Activity.RESULT_OK && uri != null) {
                onImageSelectedCallback?.invoke(uri)
            } else {
                Log.e("ImageUtils", "Image selection cancelled or failed")
            }
        }

        // Register for taking a picture with camera
        cameraLauncher = registryOwner.activityResultRegistry.register(
            "cameraLauncher", ActivityResultContracts.TakePicturePreview()
        ) { bitmap ->
            if (bitmap != null) {
                // In a real app, you'd save the bitmap to a file and get a URI
                // For simplicity here, we're focusing on the selection flow
                // You might need a different contract or logic to get a URI from camera
                Log.d("ImageUtils", "Camera image captured as bitmap")
            }
        }

        // Register permission request
        permissionLauncher = registryOwner.activityResultRegistry.register(
            "permissionLauncher", ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                Log.e("ImageUtils", "Permission denied")
            }
        }
    }

    fun launchGallery() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(permission)
        } else {
            openGallery()
        }
    }

    fun launchCamera() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Need to register a separate launcher for camera permission if strictly following this pattern
            // but for brevity using the standard flow
            activity.requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
        } else {
            cameraLauncher.launch(null)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
        }
        galleryLauncher.launch(intent)
    }
}
