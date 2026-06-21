package com.example.smart_city.repo

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import java.util.concurrent.Executors

class ImageRepoImpl : ImageRepo {

    override fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        try {
            // Using MediaManager which is part of cloudinary-android
            MediaManager.get().upload(imageUri)
                .unsigned("your_unsigned_preset") // You need to set an unsigned preset in Cloudinary console
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String?) {}
                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}
                    override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                        val imageUrl = resultData?.get("secure_url") as? String
                        callback(imageUrl)
                    }
                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        callback(null)
                    }
                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
                }).dispatch()
        } catch (e: Exception) {
            e.printStackTrace()
            callback(null)
        }
    }

    override fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }
}
