package iam.immlkit.facedetector.repos.detection

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import iam.immlkit.facedetector.model.ImageModel

/***
 * Interface for each detection repository to implement
 */
interface DetectionRepository {

    /**
     * load images from disk
     */
    fun loadFromDisk():Unit

    /**
     * Imlement image processing logic here
     */
    fun detect(application: Application)

    /**
     * used to stop detection and free resources
     */
    fun stopDetection()
}