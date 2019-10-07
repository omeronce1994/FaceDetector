package iam.immlkit.facedetector.repos.images

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import iam.immlkit.facedetector.model.ImageModel

/**
 * Interface for every image repositiory
 */
interface ImageRepository {

    /**
     * Load paginated images from room
     */
    fun getImages() : LiveData<PagedList<ImageModel>>
}