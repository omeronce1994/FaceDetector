package iam.immlkit.facedetector.model.vision

import android.content.Context
import android.net.Uri

/**
 * Vision proccesor interface to implement by every image proccesor
 */
interface VisionProcessor<T> {

    fun process(context: Context, uri: Uri)

    suspend fun processSuspended(context: Context, uri: Uri): Result<T>

    class Result<T> (val result:T)
}