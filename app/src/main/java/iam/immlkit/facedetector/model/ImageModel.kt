package iam.immlkit.facedetector.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomWarnings
import java.io.File

/**
 * Model class for images. Primary Key is image path since we add images files from disk each session of use of the app and by using as path as the primary key
 * we will avoid from adding same file twice to db
 */
@Entity
data class ImageModel(@PrimaryKey@NonNull@ColumnInfo(name = "path") val path:String,
                      @ColumnInfo(name = "isFace")val isFace:Boolean,
                      @ColumnInfo(name = "isAnalyzed")val isAnalyzed:Boolean) {

    companion object{
        fun mapObjectFromFile(imageFile: File) = ImageModel(
            path = imageFile.absolutePath,
            isFace = false,
            isAnalyzed = false
        )
    }
}