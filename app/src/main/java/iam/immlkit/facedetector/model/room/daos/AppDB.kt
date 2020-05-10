package iam.immlkit.facedetector.model.room.daos

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import iam.immlkit.facedetector.model.ImageModel
import iam.immlkit.facedetector.utils.ROOM_EXECUTOR
import iam.immlkit.facedetector.utils.ioThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.internal.Internal.instance

@Database(entities = arrayOf(ImageModel::class), version = 1)
abstract class AppDB : RoomDatabase() {

    abstract fun imagesDao(): ImagesDao

    suspend fun clearAll() = withContext(Dispatchers.IO) {
        clearAllTables()
    }
}