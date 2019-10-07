package iam.immlkit.facedetector.repos.detection

import android.app.Application
import android.util.Log
import androidx.core.content.FileProvider
import com.gabriel.chief.Managers.AppActivityManager
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import iam.immlkit.facedetector.BuildConfig
import iam.immlkit.facedetector.model.ImageModel
import iam.immlkit.facedetector.model.room.daos.ImagesDao
import iam.immlkit.facedetector.model.rxbus.RxBus
import iam.immlkit.facedetector.model.vision.VisionProcessor
import iam.immlkit.facedetector.utils.FileUtils
import kotlinx.coroutines.*
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

    val job = Job()

    val uiScope = CoroutineScope(Dispatchers.Main+job)

    @Volatile
    private var isDetecting = false

    /**
     * load all images from folder
     */
    override fun loadFromDisk() {
        uiScope.launch {
            insertAll()
        }
    }

    /**
     * Insert all images in seperate coroutine
     */
    private suspend fun insertAll() = withContext(Dispatchers.IO){
        dao.insert(FileUtils.getAllImagesFiles().map { ImageModel.mapObjectFromFile(it) })
    }

    override fun detect(application: Application) {
        //Unnecessary to run two detection process at the same time since we detecting for all images anyway
        if(isDetecting)
            return
        isDetecting=true
        uiScope.launch {
            //load only images that were not analyzed before
            val nonAnalyzedImages = getNonAnalyzedImages()
            var faceCount = 0
            //detect faces on each image
            nonAnalyzedImages.forEach {
                val result = processor.processSuspended(application,FileProvider.getUriForFile(application,BuildConfig.fileProviderPath,
                    File(it.path)
                ))
                val faces = result.result;
                Log.i(TAG,"detect: result: ${faces.size}")
                if(!faces.isEmpty())
                    faceCount++
                Log.i(TAG,"detect: facecount: ${faceCount}")
                //if faces were found mark as faceImage and mark image as analyzed
                val newModel = ImageModel(it.path,!faces.isEmpty(),true)
                updateImage(newModel)
            }
            Log.i(TAG,"detect: facecount end: ${faceCount}")
            onFinishedDetection(faceCount,nonAnalyzedImages.size)
        }
    }

    private fun onFinishedDetection(faceCount: Int, total: Int) {
        if(AppActivityManager.instance.isAppInForeground){
            RxBus.sendFinishedDetectionWithDialogEvent(faceCount,total)
        }
        else {
            RxBus.sendFinishedDetectionWithNotificationEvent(faceCount,total)
        }
    }

    /**
     * return only images that were not analyzed before
     */
    private suspend fun getNonAnalyzedImages() = withContext(Dispatchers.IO){
        dao.allNonAnalyzedImages()
    }

    /**
     * update processed image in db
     */
    private suspend fun updateImage(newModel: ImageModel) = withContext(Dispatchers.IO){
        dao.update(newModel)
    }

    override fun stopDetection() {
        job.cancel()
    }
}