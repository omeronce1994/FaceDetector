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
import org.koin.core.KoinComponent
import org.koin.core.get

/**
 * ServiceLocator provides object instances. can later be used for testing
 */
object ServiceLocator: KoinComponent {

    fun getAllImagesRepository(context: Context) = AllImagesRepository(get())

    fun getFaceImagesRepository(context: Context) = FaceImagesRepository(get())

    fun getNonFaceImagesRepository(context: Context) = NonFacesImagesRepository(get())

    fun getImagesFactory(application: Application,imageRepository: ImageRepository) = ImagesViewModelFactory(application,imageRepository)
}