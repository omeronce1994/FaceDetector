package iam.immlkit.facedetector.model.rxbus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import java.util.concurrent.ConcurrentHashMap

object LiveDataEventBus {

    private val liveDataMap : MutableMap<Class<*>,EventMutableLiveData<*>> = ConcurrentHashMap()

    fun <T: Any> subscribe(owner:LifecycleOwner,tClass: Class<T>, action: (T) -> Unit) {
        var liveData: EventMutableLiveData<T>? = liveDataMap[tClass] as EventMutableLiveData<T>?
        if(liveData==null)
            liveData = EventMutableLiveData(null)
        val eventObserver = EventObserver(action)
        liveData.observe(owner,eventObserver)
        if(!liveDataMap.containsKey(tClass))
            liveDataMap[tClass] = liveData
    }

    fun <T: Any> post(event: T){
        val tClass = event::class.java
        val liveData : MutableLiveData<Event<T>> = liveDataMap[tClass] as EventMutableLiveData<T>
        liveData.postValue(Event(event))
    }
}