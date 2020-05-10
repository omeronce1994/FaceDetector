package iam.immlkit.facedetector.model.vision.face

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import iam.immlkit.facedetector.R
import iam.immlkit.facedetector.model.Result
import iam.immlkit.facedetector.model.vision.VisionProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Exception

class FaceDetectionProcessor : VisionProcessor<MutableList<FirebaseVisionFace>> {

    companion object {

        private val TAG = "FaceDetectionProcessor"
    }

    private val detector: FirebaseVisionFaceDetector

    init {
        val options = FirebaseVisionFaceDetectorOptions.Builder()
            .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
            .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
            .build()

        detector = FirebaseVision.getInstance().getVisionFaceDetector(options)
    }

    override fun process(context: Context, uri: Uri) {

    }

    /**
     * Suspened process image to use in coroutines
     */
    override suspend fun processSuspended(
        context: Context,
        uri: Uri
    ): Result<MutableList<FirebaseVisionFace>> {
        //load image file to ML-KIT FirebaseVisionImage object
        val image: FirebaseVisionImage
        var result: Result<MutableList<FirebaseVisionFace>>
        try {
            image = FirebaseVisionImage.fromFilePath(context, uri)
            result =  Result.Success(getFaces(image))
        } catch (e: IOException) {
            e.printStackTrace()
            result = Result.Error(e)
        }
        return result
    }

    /**
     * suspend function to detect face in image using ML KIT detector
     */
    private suspend fun getFaces(image: FirebaseVisionImage) = withContext(Dispatchers.IO){
        detector.detectInImage(image).await()
    }

}