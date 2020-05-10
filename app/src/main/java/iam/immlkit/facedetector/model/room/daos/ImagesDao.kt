package iam.immlkit.facedetector.model.room.daos

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import iam.immlkit.facedetector.model.ImageModel

/**
 * Database Access Object for the ImageModel database.
 */

@Dao
interface ImagesDao {

    /**
     * Return datasource factory for paging. Room will update live data returned from DataFactory.toLiveData each time db is updated
     */

    /**
     * Return All Images in DB
     */
    @Query("SELECT * FROM ImageModel ORDER BY path COLLATE NOCASE DESC")
    fun allImages(): DataSource.Factory<Int, ImageModel>

    /**
     * Return Only Faces Images. Boolean values are stored as integers 0 (false) and 1 (true) in SQLITE
     */
    @Query("SELECT * FROM ImageModel WHERE isFace=1 AND isAnalyzed=1 ORDER BY path COLLATE NOCASE DESC")
    fun allFacesImages(): DataSource.Factory<Int, ImageModel>

    /**
     * Return Only NonFaces Images. Boolean values are stored as integers 0 (false) and 1 (true) in SQLITE
     */
    @Query("SELECT * FROM ImageModel WHERE isFace=0 AND isAnalyzed=1 ORDER BY path COLLATE NOCASE DESC")
    fun allNonFacesImages(): DataSource.Factory<Int, ImageModel>

    /**
     * Return Only NonFaces Images. Boolean values are stored as integers 0 (false) and 1 (true) in SQLITE
     */
    @Query("SELECT * FROM ImageModel WHERE isFace = 0 AND isAnalyzed = 0 ORDER BY path COLLATE NOCASE DESC")
    suspend fun allNonAnalyzedImages(): List<ImageModel>

    /**
     * Insert Multiple items in one transaction. Ignore on conflict so that on new session when we will load from folder file with same path wont override
     * already analyzed images
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(imageModels: List<ImageModel>)

    /**
     * Insert single item in one transaction. Ignore on conflict so that on new session when we will load from folder file with same path wont override
     * already analyzed images
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(imageModel: ImageModel)

    /**
     * Update Multiple items in one transaction
     */
    @Update
    fun update(imageModels: List<ImageModel>)

    /**
     * Update single item in one transaction
     */
    @Update
    suspend fun update(imageModels: ImageModel)
}