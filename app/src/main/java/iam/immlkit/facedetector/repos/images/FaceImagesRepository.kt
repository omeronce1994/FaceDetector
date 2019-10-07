package iam.immlkit.facedetector.repos.images

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import iam.immlkit.facedetector.model.ImageModel
import iam.immlkit.facedetector.model.room.daos.ImagesDao
import iam.immlkit.facedetector.utils.pagedListConfig
import iam.immlkit.facedetector.utils.toLiveData

/**
 * class to load only face images from db
 */
class FaceImagesRepository(private val dao: ImagesDao) :ImageRepository {

    override fun getImages(): LiveData<PagedList<ImageModel>> = dao.allFacesImages().toLiveData(
        pagedListConfig()
    )
}