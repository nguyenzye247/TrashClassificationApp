package com.dut.trashdetect.ui.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.dut.trashdetect.R
import com.dut.trashdetect.base.BaseActivity
import com.dut.trashdetect.base.BaseInput
import com.dut.trashdetect.base.ViewModelProviderFactory
import com.dut.trashdetect.databinding.ActivityMainBinding
import com.dut.trashdetect.ml.AutoModel25062022
import org.tensorflow.lite.support.common.TensorProcessor
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.label.TensorLabel


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    private val trashModel by lazy { AutoModel25062022.newInstance(this@MainActivity) }

    override fun getLazyBinding() = lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun getLazyViewModel() = viewModels<MainViewModel> {
        ViewModelProviderFactory(BaseInput.NoInput)
    }

    override fun setupInit() {
        initViews()
        initListener()
        observe()
    }

    companion object {
        private val CLASSES = arrayListOf(
            "battery",
            "biological",
            "cardboard",
            "clothes",
            "glass",
            "metal",
            "paper",
            "plastic",
            "trash"
        )
    }

    private fun initViews() {

    }

    private fun initListener() {
        binding.apply {
            btnClassify.setOnClickListener {
                val imageBitmap = BitmapFactory.decodeResource(
                    resources,
                    R.drawable.cardboard1
                )
                resultGenerator(imageBitmap)
            }
        }
    }

    private fun observe() {

    }

    private fun resultGenerator(bitmap: Bitmap) {
        binding.pgLoading.isVisible = true
        Thread {
            try {
                // convert bitmap to tensorflow image with size=128x128
                val newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                var inputImage = TensorImage.fromBitmap(newBitmap)
                inputImage = getImageProcessor().process(inputImage)

                // process the image using trained model and sort it in descending order
                val processedOutput =
                    trashModel.process(getNormalization().process(inputImage.tensorBuffer))
                // getting the output buffer
                val outputBuffer = processedOutput.outputFeature0AsTensorBuffer
                // adding labels to the output
                val tensorLabel = TensorLabel(CLASSES, outputBuffer)
                // getting each class's probability
                val probabilities = hashMapOf<String, Float>()
                CLASSES.forEach { classType ->
                    tensorLabel.mapWithFloatValue[classType]?.let { prob ->
                        probabilities[classType] = prob
                    }
                }
                Log.d("CLASSIFY", "$probabilities")
                runOnUiThread {
                    binding.pgLoading.isVisible = false
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

    private fun getImageProcessor(): ImageProcessor {
        return ImageProcessor.Builder()
            .add(ResizeOp(128, 128, ResizeOp.ResizeMethod.BILINEAR))
            .build()
    }

    private fun getNormalization(): TensorProcessor {
        return TensorProcessor.Builder()
            .add(NormalizeOp(0f, 255f))
            .build()
    }

    override fun onDestroy() {
        trashModel.close()
        super.onDestroy()
    }
}
