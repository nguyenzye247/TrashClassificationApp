package com.dut.trashdetect.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object CameraUtils {
    var currentPhotoPath: String = ""
    fun requestOpenCamera(activity: Activity,
                          takePictureLauncher: ActivityResultLauncher<String>,
                          captureLauncher: ActivityResultLauncher<Intent>
    ) {
        //Request for camera permission
        if (Helpers.shouldAskPermission()) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openCamera(activity, captureLauncher)
            } else if (activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                takePictureLauncher.launch(Manifest.permission.CAMERA)
            } else {
                takePictureLauncher.launch(Manifest.permission.CAMERA)
            }
        } else {
            openCamera(activity, captureLauncher)
        }
    }

    fun openCamera(activity: Activity, captureLauncher: ActivityResultLauncher<Intent>) {
        // Open camera
        val camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        captureLauncher.launch(camIntent)
        if (camIntent.resolveActivity(activity.packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile(activity)
            } catch (ex: IOException) {
                Log.e("FILE_ERROR", "Error file")
            }
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    activity,
                    "com.android.broadcast_receiver",
                    photoFile
                )
                camIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                camIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                captureLauncher.launch(camIntent)
            }
        } else {
            Toast.makeText(
                activity, "There are no app that support this action,",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @Throws(IOException::class)
    fun createImageFile(context: Context): File? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.absolutePath
        return image
    }
}
