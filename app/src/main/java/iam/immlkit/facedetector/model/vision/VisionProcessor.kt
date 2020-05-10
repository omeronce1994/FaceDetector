package iam.immlkit.facedetector.model.vision

import android.content.Context
import android.net.Uri
import iam.immlkit.facedetector.model.Result

/**
 * Vision proccesor interface to implement by every image proccesor
 */
interface VisionProcessor<out T: Any> {

    fun process(context: Context, uri: Uri)

    suspend fun processSuspended(context: Context, uri: Uri): Result<T>
}