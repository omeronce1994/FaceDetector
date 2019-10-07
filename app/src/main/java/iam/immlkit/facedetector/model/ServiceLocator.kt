package iam.immlkit.facedetector.model

import android.app.Application
import android.content.Context
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import iam.immlkit.facedetector.model.room.daos.AppDB
import iam.immlkit.facedetector.model.vision.VisionProcessor
import iam.immlkit.facedetector.model.vision.face.FaceDetectionProcessor
import iam.immlkit.facedetector.repos.detection.DetectionRepositoryImpl
import iam.immlkit.facedetector.repos.images.AllImagesRepository
import iam.immlkit.facedetector.repos.images.FaceImagesRepository
import iam.immlkit.facedetector.repos.images.ImageRepository
import iam.immlkit.facedetector.repos.images.NonFacesImagesRepository
import iam.immlkit.facedetector.viewmodel.images.ImagesViewModelFactory

/**
 * ServiceLocator provides object instances. can later be used for testing
 */
object ServiceLocator {

    fun getAppDB(context: Context) = AppDB.getInstance(context)

    fun getFaceDetectionProcessor() = FaceDetectionProcessor()

    fun getDetectionRepository(db:AppDB,processor:  VisionProcessor<MutableList<FirebaseVisionFace>>) = DetectionRepositoryImpl.getInstance(db.imagesDao(),processor)

    fun getAllImagesRepository(db: AppDB) = AllImagesRepository(db.imagesDao())

    fun getFaceImagesRepository(db: AppDB) = FaceImagesRepository(db.imagesDao())

    fun getNonFaceImagesRepository(db: AppDB) = NonFacesImagesRepository(db.imagesDao())

    fun getImagesFactory(application: Application,imageRepository: ImageRepository) = ImagesViewModelFactory(application,imageRepository)
}