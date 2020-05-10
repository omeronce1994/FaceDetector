package iam.immlkit.facedetector.model

import java.lang.Exception

sealed class Result<out T: Any> {
    data class Success<out T: Any>(val value: T): Result<T>()
    data class Error(val exception: Exception): Result<Nothing>()
}