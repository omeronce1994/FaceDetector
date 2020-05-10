package iam.immlkit.facedetector.repos.detection

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import iam.immlkit.facedetector.model.ImageModel
import iam.immlkit.facedetector.model.Result
import iam.immlkit.facedetector.model.vision.face.FaceResults

/***
 * Interface for each detection repository to implement
 */
interface DetectionRepository {

    /**
     * load images from disk
     */
    suspend fun loadFromDisk():Unit

    /**
     * Imlement image processing logic here
     */
    suspend fun detect(application: Application): Result<FaceResults>
}