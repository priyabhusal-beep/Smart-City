package com.example.smart_city.repo

import android.content.Context
import android.net.Uri

interface ImageRepo {
    fun uploadImage(
        context: Context,
        imageUri: Uri,
        callback: (String?) -> Unit
    )

    fun getFileNameFromUri(context: Context, uri: Uri): String?
}
