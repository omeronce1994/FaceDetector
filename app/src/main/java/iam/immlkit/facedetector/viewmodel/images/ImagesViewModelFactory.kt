package iam.immlkit.facedetector.viewmodel.images

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import iam.immlkit.facedetector.repos.detection.DetectionRepository
import iam.immlkit.facedetector.repos.images.AllImagesRepository
import iam.immlkit.facedetector.repos.images.FaceImagesRepository
import iam.immlkit.facedetector.repos.images.ImageRepository
import iam.immlkit.facedetector.repos.images.NonFacesImagesRepository

/**
 * Factory for instantiating images view model by their repository dependecy type
 */
class ImagesViewModelFactory(val application: Application,val imageRepository: ImageRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when (imageRepository){
            is AllImagesRepository -> return getAllImagesViewModel(modelClass)
            is FaceImagesRepository -> return getFacesImagesViewModel(modelClass)
            is NonFacesImagesRepository -> return getNonFacesImagesViewModel(modelClass)
            else -> throw java.lang.IllegalArgumentException("No matching view model for ${modelClass.canonicalName}")
        }
    }

    private fun <T : ViewModel?> getAllImagesViewModel(modelClass: Class<T>) = if (modelClass.isAssignableFrom(AllImagesViewModel::class.java!!)) {
        AllImagesViewModel(application,imageRepository) as T
    } else {
        throw IllegalArgumentException("ViewModel Not Found")
    }

    private fun <T : ViewModel?> getFacesImagesViewModel(modelClass: Class<T>) = if (modelClass.isAssignableFrom(FaceImagesViewModel::class.java!!)) {
        FaceImagesViewModel(application,imageRepository) as T
    } else {
        throw IllegalArgumentException("ViewModel Not Found")
    }

    private fun <T : ViewModel?> getNonFacesImagesViewModel(modelClass: Class<T>) = if (modelClass.isAssignableFrom(NonFacesImagesViewModel::class.java!!)) {
        NonFacesImagesViewModel(application,imageRepository) as T
    } else {
        throw IllegalArgumentException("ViewModel Not Found")
    }
}