package com.example.smart_city.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smart_city.repo.ImageRepo
import com.example.smart_city.repo.ImageRepoImpl

class ImageViewModel(private val repo: ImageRepo = ImageRepoImpl()) : ViewModel() {

    // NEW: tracks whether an upload is currently in progress
    var isUploading by mutableStateOf(false)
        private set

    fun uploadImage(
        context: Context,
        imageUri: Uri,
        callback: (String?) -> Unit
    ) {
        Log.d("ImageViewModel", "uploadImage called with URI: $imageUri")
        isUploading = true

        repo.uploadImage(context, imageUri) { url ->
            Log.d("ImageViewModel", "Upload result received: $url")
            isUploading = false
            callback(url)
        }
    }
}