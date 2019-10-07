package iam.immlkit.facedetector.viewmodel.detection

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import iam.immlkit.facedetector.repos.detection.DetectionRepository
import iam.immlkit.facedetector.viewmodel.images.AllImagesViewModel

/**
 * Factory for instantiating detection view model
 */
class DetectionViewModelFactory (val application: Application, val detectionRepository: DetectionRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DetectionViewModel::class.java!!)) {
            DetectionViewModel(application, detectionRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}