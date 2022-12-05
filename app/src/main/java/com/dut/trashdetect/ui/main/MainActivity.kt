package com.dut.trashdetect.ui.main

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.dut.trashdetect.R
import com.dut.trashdetect.base.BaseActivity
import com.dut.trashdetect.base.BaseInput
import com.dut.trashdetect.base.ViewModelProviderFactory
import com.dut.trashdetect.common.RESULT_DOC
import com.dut.trashdetect.databinding.ActivityMainBinding
import com.dut.trashdetect.extention.hideMini
import com.dut.trashdetect.extention.showMini
import com.dut.trashdetect.ml.AutoModel25062022
import com.dut.trashdetect.model.Result
import com.dut.trashdetect.model.UserResult
import com.dut.trashdetect.ui.history.HistoryActivity
import com.dut.trashdetect.util.CameraUtils
import com.dut.trashdetect.util.ClassificationUtils
import com.dut.trashdetect.util.Helpers
import com.dut.trashdetect.util.Helpers.roundPercent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileNotFoundException
import kotlin.math.roundToInt


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    private val trashModel by lazy { AutoModel25062022.newInstance(this@MainActivity) }
    private var isMenuOpened = false
    private var probabilities = hashMapOf<String, Float>()
    private val firebaseFireStore = FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()
    private val storageRef = firebaseStorage.reference

    private val getGalleryImageResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { getResultFrom(it) }

    private val captureImageActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            setCapturedPic()
        }
    }

    private lateinit var takePicturePermissionResultLauncher: ActivityResultLauncher<String>

    override fun getLazyBinding() = lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun getLazyViewModel() = viewModels<MainViewModel> {
        ViewModelProviderFactory(BaseInput.NoInput)
    }

    override fun setupInit() {
        initViews()
        initListener()
        initLauncher()
    }

    companion object {
        private val CLASSES = arrayListOf(
            "Battery",
            "Biological",
            "Cardboard",
            "Clothes",
            "Glass",
            "Metal",
            "Paper",
            "Plastic",
            "Trash"
        )
        private const val TRANSLATE_RIGHT = 100f
        private const val TRANSLATE_LEFT = -100f
        private const val TRANSLATE_BOTTOM = 100f
    }

    private fun initViews() {
        binding.apply {
            btnGallery.apply {
                alpha = 0f
                scaleX = 0f
                scaleY = 0f
                translationX = TRANSLATE_RIGHT
            }
            btnTakePhoto.apply {
                alpha = 0f
                scaleX = 0f
                scaleY = 0f
                translationX = TRANSLATE_LEFT
            }
            btnUser.apply {
                alpha = 0f
                scaleX = 0f
                scaleY = 0f
                translationY = TRANSLATE_BOTTOM
            }
            tvResult.text = getString(R.string.select_picture)
            btnInfo.isVisible = false
        }
    }

    private fun initListener() {
        binding.apply {
            btnSelectImage.setOnClickListener {
                if (isMenuOpened) {
                    closeMenu()
                } else {
                    openMenu()
                }
            }
            btnGallery.setOnClickListener {
                closeMenu()
                openGallery()
            }
            btnTakePhoto.setOnClickListener {
                closeMenu()
                takePhoto()
            }
            btnUser.setOnClickListener {
                closeMenu()
                goToHistory()
            }
            btnInfo.setOnClickListener {
                showInfoBottomSheet()
            }
            btnLogOut.setOnClickListener {
                confirmLogout()
            }
        }
    }

    private fun initLauncher() {
        takePicturePermissionResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                CameraUtils.openCamera(this@MainActivity, captureImageActivityResultLauncher)
            } else if (Helpers.shouldAskPermission() && !shouldShowRequestPermissionRationale(
                    Manifest.permission.CAMERA
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle(R.string.permis_denied)
                    .setMessage(R.string.permis_unable_message)
                    .setPositiveButton(
                        R.string.go_to_setting
                    ) { _, _ ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts(
                            "package",
                            packageName, null
                        )
                        intent.data = uri
                        startActivity(intent)
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Permission Denied")
                    .setMessage(R.string.permis_denied_message)
                    .setPositiveButton(R.string.sure, null)
                    .setNegativeButton(
                        R.string.let_allow
                    ) { _, _ ->
                        takePicturePermissionResultLauncher.launch(
                            Manifest.permission.CAMERA
                        )
                    }
                    .show()
            }
        }
    }

    private fun openMenu() {
        binding.apply {
            btnGallery.showMini(300L, 1f, 1f)
            btnTakePhoto.showMini(300L, 1f, 1f)
            btnUser.showMini(300L, 1f, 1f)
        }
        isMenuOpened = !isMenuOpened
    }

    private fun closeMenu() {
        binding.apply {
            btnGallery.hideMini(300L, TRANSLATE_RIGHT, 1f)
            btnTakePhoto.hideMini(300L, TRANSLATE_LEFT, 1f)
            btnUser.hideMini(300L, 1f, TRANSLATE_BOTTOM)
        }
        isMenuOpened = !isMenuOpened
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        getGalleryImageResultLauncher.launch(intent)
    }

    private fun takePhoto() {
        CameraUtils.requestOpenCamera(
            this@MainActivity,
            takePicturePermissionResultLauncher,
            captureImageActivityResultLauncher
        )
    }

    private fun goToHistory() {
        startActivity(
            Intent(
                this@MainActivity,
                HistoryActivity::class.java
            )
        )
    }

    private fun classify(bitmap: Bitmap?, path: Uri) {
        bitmap?.let {
            resultGenerator(it, path)
        }
    }

    private fun resultGenerator(bitmap: Bitmap, path: Uri) {
        binding.pgLoading.isVisible = true
        Thread {
            try {
                binding.tvResult.isVisible = false
                probabilities = ClassificationUtils.doClassify(trashModel, bitmap, CLASSES)
                runOnUiThread {
                    setResultViews(path)
                }
            } catch (ex: Exception) {
                runOnUiThread {
                    binding.pgLoading.isVisible = false
                    Toast.makeText(this@MainActivity, "Error classifying image", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }.start()
    }

    private fun setResultViews(path: Uri) {
        binding.apply {
            pgLoading.isVisible = false
            btnInfo.isVisible = true
            tvResult.isVisible = true
            val maxClass = ClassificationUtils.getHighestClass(probabilities)
            val result = "Is ${maxClass.key}"
            val textPercent = roundPercent(maxClass.value)
            tvResult.text = result
            tvResultPercentage.text = textPercent
            saveAndGetImageUrl(
                path,
                maxClass.key,
                ((maxClass.value * 1000.0).roundToInt() / 1000.0).toFloat()
            )
        }
    }

    private fun saveAndGetImageUrl(path: Uri, maxClass: String, percent: Float) {
        val resultRef = storageRef.child("result_images/${System.currentTimeMillis()}")
        val uploadTask = resultRef.putFile(path)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            resultRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                saveFirebaseResult(maxClass, percent, downloadUri.toString())
            } else {
                Toast.makeText(this@MainActivity, "Failed to get image", Toast.LENGTH_SHORT)
                    .show()
                saveFirebaseResult(maxClass, percent, "")
            }
        }
    }

    private fun getResultFrom(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            try {
                val imageUri = result.data?.data
                imageUri?.let {
                    val imageStream = contentResolver.openInputStream(it)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    binding.ivResult.setImageBitmap(selectedImage)
                    classify(selectedImage, it)
                    imageStream?.close()
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this@MainActivity, "No picture selected", Toast.LENGTH_LONG).show()
        }
    }

    private fun showInfoBottomSheet() {
        val resultBottomSheet = ResultBottomSheet.newInstance(probabilities)
        resultBottomSheet.show(supportFragmentManager, ResultBottomSheet.TAG)
    }

    override fun onDestroy() {
        trashModel.close()
        super.onDestroy()
    }

    private fun setCapturedPic() {
        // Get the dimensions of the View
        val targetW: Int = binding.cvSelectedImage.width
        val targetH: Int = binding.cvSelectedImage.height
        // Get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(CameraUtils.currentPhotoPath, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight
        // Determine how much to scale down the image
        val scaleFactor = (photoW / targetW).coerceAtMost(photoH / targetH)
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        // Set views
        val bitmap = BitmapFactory.decodeFile(CameraUtils.currentPhotoPath, bmOptions)
        binding.ivResult.setImageBitmap(bitmap)
        val uri = Uri.fromFile(File(CameraUtils.currentPhotoPath))
        classify(bitmap, uri)
    }

    private fun confirmLogout() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage(R.string.logout_confirm)
            .setPositiveButton(R.string.yes) { _, _ ->
                logout()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private fun saveFirebaseResult(name: String, probability: Float, imageUrl: String) {
        FirebaseAuth.getInstance().uid?.let {
            firebaseFireStore.collection(RESULT_DOC)
                .add(
                    UserResult(
                        it,
                        System.currentTimeMillis(),
                        Result(name, probability),
                        imageUrl
                    )
                )
        }
    }

    private fun logout() {
        FirebaseAuth.getInstance()
            .signOut()
    }
}
