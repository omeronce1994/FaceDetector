package iam.immlkit.facedetector.utils

import android.content.Context
import android.os.Environment
import androidx.core.content.FileProvider
import iam.immlkit.facedetector.BuildConfig
import java.io.File

class FileUtils {

    companion object{
        //parent dir path
        val rootDirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath+"/anyvision"
        //sub dir path
        val fullDirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath+"/anyvision/folder"

        fun getDownloadFolderUri(context: Context,file: File) = FileProvider.getUriForFile(context,BuildConfig.fileProviderPath,file)

        fun getAllImagesFiles() : List<File>{
            //in order to get to sub dir needed to mk parent dirs first if not exist or an exception will be thrown
            val rootDir = File(rootDirPath)
            if(!rootDir.exists())
                rootDir.mkdir()
            val fullDir = File(fullDirPath)
            if(!fullDir.exists())
                fullDir.mkdir()
            return fullDir.listFiles().asList()
        }
    }
}