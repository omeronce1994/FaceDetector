package iam.immlkit.facedetector.repos.images

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import iam.immlkit.facedetector.model.ImageModel
import iam.immlkit.facedetector.model.room.daos.ImagesDao
import iam.immlkit.facedetector.utils.pagedListConfig
import iam.immlkit.facedetector.utils.toLiveData

/**
 * class to load only non faces images from db
 */
class NonFacesImagesRepository(private val dao: ImagesDao): ImageRepository {

    override fun getImages(): LiveData<PagedList<ImageModel>> = dao.allNonFacesImages().toLiveData(
        pagedListConfig()
    )
}