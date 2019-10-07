package iam.immlkit.facedetector.model.room.daos

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import iam.immlkit.facedetector.model.ImageModel
import iam.immlkit.facedetector.utils.ROOM_EXECUTOR
import iam.immlkit.facedetector.utils.ioThread

@Database(entities = arrayOf(ImageModel::class), version = 1)
abstract class AppDB : RoomDatabase() {

    abstract fun imagesDao(): ImagesDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDB? = null

        fun getInstance(context: Context): AppDB {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDB {
            return Room.databaseBuilder(context, AppDB::class.java, "images-db")
                .setTransactionExecutor(ROOM_EXECUTOR)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}