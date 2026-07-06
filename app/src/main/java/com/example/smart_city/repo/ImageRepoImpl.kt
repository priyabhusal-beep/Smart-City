package com.example.smart_city.repo

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

class ImageRepoImpl : ImageRepo {

    private val client = OkHttpClient()
    private val CLOUD_NAME = "dyanvmmkj"
    private val UPLOAD_PRESET = "smartcity_upload"

    override fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        Log.d("ImageUpload", "Starting direct HTTP upload for: $imageUri")

        try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            if (inputStream == null) {
                Log.e("ImageUpload", "Cannot open input stream for URI")
                Handler(Looper.getMainLooper()).post { callback(null) }
                return
            }

            val bytes = inputStream.readBytes()
            inputStream.close()

            val fileName = getFileNameFromUri(context, imageUri) ?: "profile_${System.currentTimeMillis()}.jpg"
            val mimeType = context.contentResolver.getType(imageUri) ?: "image/jpeg"

            Log.d("ImageUpload", "File: $fileName, size: ${bytes.size}, type: $mimeType")

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName,
                    RequestBody.create(mimeType.toMediaTypeOrNull(), bytes))
                .addFormDataPart("upload_preset", UPLOAD_PRESET)
                .build()

            val request = Request.Builder()
                .url("https://api.cloudinary.com/v1_1/$CLOUD_NAME/image/upload")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("ImageUpload", "Upload failed: ${e.message}")
                    Handler(Looper.getMainLooper()).post { callback(null) }
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    Log.d("ImageUpload", "Response: $body")
                    if (response.isSuccessful && body != null) {
                        // Parse secure_url from JSON manually
                        val urlMatch = Regex("\"secure_url\":\"([^\"]+)\"").find(body)
                        val url = urlMatch?.groupValues?.get(1)?.replace("\\/", "/")
                        Log.d("ImageUpload", "Extracted URL: $url")
                        Handler(Looper.getMainLooper()).post { callback(url) }
                    } else {
                        Log.e("ImageUpload", "Upload unsuccessful: ${response.code}")
                        Handler(Looper.getMainLooper()).post { callback(null) }
                    }
                }
            })

        } catch (e: Exception) {
            Log.e("ImageUpload", "Exception during upload: ${e.message}")
            Handler(Looper.getMainLooper()).post { callback(null) }
        }
    }

    override fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) fileName = it.getString(nameIndex)
            }
        }
        return fileName
    }
}