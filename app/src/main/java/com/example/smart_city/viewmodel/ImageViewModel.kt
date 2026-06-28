package com.example.smart_city.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.smart_city.repo.ImageRepo
import com.example.smart_city.repo.ImageRepoImpl

class ImageViewModel(private val repo: ImageRepo = ImageRepoImpl()) : ViewModel() {

    fun uploadImage(
        context: Context,
        imageUri: Uri,
        callback: (String?) -> Unit
    ) {
        Log.d("ImageViewModel", "uploadImage called with URI: $imageUri")
        repo.uploadImage(context, imageUri) { url ->
            Log.d("ImageViewModel", "Upload result received: $url")
            callback(url)
        }
    }
}