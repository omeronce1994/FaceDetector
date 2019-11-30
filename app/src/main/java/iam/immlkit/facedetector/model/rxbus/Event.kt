package iam.immlkit.facedetector.model.rxbus

sealed class Event<T>(val event: T) {

    private var hasBeenHandled = false
    get() = field
    set(value) {
        field =value}

    fun event() = event
}