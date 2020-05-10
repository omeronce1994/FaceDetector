package iam.immlkit.facedetector.di.module

import com.google.firebase.ml.vision.face.FirebaseVisionFace
import iam.immlkit.facedetector.model.room.daos.ImagesDao
import iam.immlkit.facedetector.model.vision.VisionProcessor
import iam.immlkit.facedetector.model.vision.face.FaceDetectionProcessor
import iam.immlkit.facedetector.repos.detection.DetectionRepository
import iam.immlkit.facedetector.repos.detection.DetectionRepositoryImpl
import iam.immlkit.facedetector.viewmodel.detection.DetectionViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val detectionModule = module {
    single<DetectionRepository> { provideDetectionRepositoryImpl(get())}
    factory { DetectionViewModel(androidApplication(), get())}
}
fun provideDetectionRepositoryImpl(dao: ImagesDao, processor: VisionProcessor<MutableList<FirebaseVisionFace>> = FaceDetectionProcessor()) = DetectionRepositoryImpl(dao, processor)