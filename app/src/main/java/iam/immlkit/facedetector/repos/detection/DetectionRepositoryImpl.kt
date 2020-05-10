package iam.immlkit.facedetector.repos.detection

import android.app.Application
import android.media.Image
import android.util.Log
import androidx.core.content.FileProvider
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import iam.immlkit.facedetector.BuildConfig
import iam.immlkit.facedetector.model.ImageModel
import iam.immlkit.facedetector.model.Result
import iam.immlkit.facedetector.model.room.daos.ImagesDao
import iam.immlkit.facedetector.model.rxbus.RxBus
import iam.immlkit.facedetector.model.vision.VisionProcessor
import iam.immlkit.facedetector.model.vision.face.FaceResults
import iam.immlkit.facedetector.utils.FileUtils
import kotlinx.coroutines.*
import omeronce.android.smartactivitymanager.SmartActivityManager
import omeronce.android.smartactivitymanager.enums.AppState
import java.io.File

class DetectionRepositoryImpl private constructor(private val dao: ImagesDao,private val processor:  VisionProcessor<MutableList<FirebaseVisionFace>>) :
    DetectionRepository {

    companion object{

        val TAG = "DetectionRepo"

        @Volatile
        private var instance: DetectionRepositoryImpl? = null

        fun getInstance(dao: ImagesDao,processor:  VisionProcessor<MutableList<FirebaseVisionFace>>): DetectionRepositoryImpl {
            return instance
                ?: synchronized(this) {
                instance
                    ?: DetectionRepositoryImpl(dao,processor).also { instance = it }
            }
        }
    }

    /**
     * load all images from folder
     */
    override suspend fun loadFromDisk() {
        dao.insert(FileUtils.getAllImagesFiles().map { ImageModel.mapObjectFromFile(it) })
    }

    override suspend fun detect(application: Application): Result<FaceResults> {
        //load only images that were not analyzed before
        val nonAnalyzedImages = dao.allNonAnalyzedImages()
        var faceCount = 0
        //detect faces on each image
        nonAnalyzedImages.forEach {
            val result = processor.processSuspended(application,FileProvider.getUriForFile(application,BuildConfig.fileProviderPath,
                File(it.path)
            ))
            //if faces were found mark as faceImage and mark image as analyzed
            val newModel = handleResult(it, result)
            if(newModel.isFace)
                faceCount++
            Log.i(TAG,"detect: facecount: ${faceCount}")
            dao.update(newModel)
        }
        Log.i(TAG,"detect: facecount end: ${faceCount}")
        return Result.Success(FaceResults(faceCount, nonAnalyzedImages.size))
    }

    private fun handleResult(nonAnalyzedImage: ImageModel, result: Result<MutableList<FirebaseVisionFace>>): ImageModel{
        return when (result) {
            is Result.Success -> ImageModel(nonAnalyzedImage.path, result.value.isNotEmpty(),true)
            is Result.Error -> ImageModel(nonAnalyzedImage.path, false, false)
        }
    }
}