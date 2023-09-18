package com.dut.trashdetect.util

import android.graphics.Bitmap
import android.util.Log
import com.dut.trashdetect.ml.AutoModel25062022
import org.tensorflow.lite.support.common.TensorProcessor
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.label.TensorLabel

object ClassificationUtils {
    fun getHighestClass(probabilities: HashMap<String, Float>): Map.Entry<String, Float> {
        return probabilities.maxBy { it.value }
    }

    fun doClassify(
        trashModel: AutoModel25062022,
        bitmap: Bitmap,
        classes: ArrayList<String>
    ): HashMap<String, Float> {
        val probabilities = hashMapOf<String, Float>()
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
        val tensorLabel = TensorLabel(classes, outputBuffer)
        // getting each class's probability
        classes.forEach { classType ->
            tensorLabel.mapWithFloatValue[classType]?.let { prob ->
                probabilities[classType] = prob
            }
        }
        Log.d("CLASSIFY", "$probabilities")
        return probabilities
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
}
