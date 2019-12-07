package iam.immlkit.facedetector.model.rxbus

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.MutableLiveData

class EventMutableLiveData<T>(value: Event<T>?) : MutableLiveData<Event<T>>(value) {

    override fun setValue(value: Event<T>?) {
        super.setValue(value)
        //mark as hasbeenhandled only after calling super and all subscribed observers hs been called
        value?.hasBeenHandled = true
    }
}