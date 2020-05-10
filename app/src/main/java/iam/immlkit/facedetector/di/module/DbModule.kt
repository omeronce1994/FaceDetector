package iam.immlkit.facedetector.di.module

import android.content.Context
import androidx.room.Room
import iam.immlkit.facedetector.model.room.daos.AppDB
import iam.immlkit.facedetector.utils.ROOM_EXECUTOR
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {
    single {  buildDb(androidContext())  }
    single { get<AppDB>().imagesDao() }
}

fun buildDb(context: Context) =
    Room.databaseBuilder(context, AppDB::class.java, "images-db")
        .setTransactionExecutor(ROOM_EXECUTOR)
        .fallbackToDestructiveMigration()
        .build()
