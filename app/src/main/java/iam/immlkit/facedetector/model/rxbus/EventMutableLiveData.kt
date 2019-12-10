package iam.immlkit.facedetector.model.rxbus

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.MutableLiveData

class EventMutableLiveData<T>(value: Event<T>?) : MutableLiveData<Event<T>>(value) {


    override fun setValue(value: Event<T>?) {
        super.setValue(value)
        //mark as hasbeenhandled only after calling super and all subscribed observers hs been called
        value?.hasBeenHandled = true
    }

    override fun postValue(value: Event<T>?) {
        super.postValue(value)
        //execute on main thread using same executor live data is using to make sure we mark event as handled after it executed all of it observers
        ArchTaskExecutor.getInstance().postToMainThread{value?.hasBeenHandled = true}
    }
}