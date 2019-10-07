package iam.immlkit.facedetector.utils

import androidx.arch.core.executor.ArchTaskExecutor
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Set single thread executor so we will always get most recent data
 */
val ROOM_EXECUTOR = Executors.newSingleThreadExecutor()

/**
 * Utility method to run blocks on a dedicated background thread, used for io/database work.
 */
fun ioThread(f : () -> Unit) {
    ROOM_EXECUTOR.execute(f)
}