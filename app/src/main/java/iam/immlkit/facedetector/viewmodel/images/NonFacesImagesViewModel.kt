package iam.immlkit.facedetector.viewmodel.images

import android.app.Application
import iam.immlkit.facedetector.repos.images.ImageRepository
import iam.immlkit.facedetector.viewmodel.BaseViewModel

/**
 * ViewModel class to handle NonFaces image logic
 */
class NonFacesImagesViewModel (application: Application, private val repository: ImageRepository) : BaseViewModel(application) {

    //LiveData from db to observe and get notified automatically (cool feature of Room API) when db is updated
    val allNonFacesImages = repository.getImages()

}