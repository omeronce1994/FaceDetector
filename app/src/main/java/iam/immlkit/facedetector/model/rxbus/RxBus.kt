package iam.immlkit.facedetector.model.rxbus

import android.util.Log
import androidx.arch.core.internal.SafeIterableMap
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import iam.immlkit.facedetector.model.rxbus.events.OnFinishedDetectionWithDialog
import iam.immlkit.facedetector.model.rxbus.events.OnFinishedDetectionWithNotification

import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action0
import rx.functions.Action1
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject
import java.util.*
import kotlin.collections.HashSet


/**
 * EXAMPLE FOR LISTENING TO A CERTAIN EVENT (WE CAN USE ANY OBJECT WE LIKE, IN THIN CASE WE USE OnFinishedDetectionWithNotification CLASS)
    listenerTag - an object used to identify listeners later for removal

    RxBus.listenFor(TAG,OnFinishedDetectionWithNotification::class.java,object : Action1<OnFinishedDetectionWithNotification>{
        override fun call(t: OnFinishedDetectionWithNotification?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    })

 *To remove observer just call removeListener(TAG)
 */


object RxBus {

    private val subject = SerializedSubject(PublishSubject.create<Any>())
    private val listenersTracker = HashMap<Any, HashSet<Subscription>>()
    private val observerTracker = mutableMapOf<LifecycleOwner,HashSet<Observer<*>>>()
    private val liveDataMap = mutableMapOf<Class<*>,MutableLiveData<Event<Any>>>()

    private val onError =
        Action1<Throwable> { o -> Log.i("rxbus", "onError called" + o.javaClass.toString()) }

    private val onCompleted = Action0 { Log.i("rxbus", "onCompleted called") }

    fun <T> listenFor(listenerTag: Any, tClass: Class<T>, handler: Action1<T>): Subscription {
        //        Subscription subscription = subject.ofType(tClass).observeOn(AndroidSchedulers.mainThread()).subscribe(handler);
        val subscription = subject.ofType(tClass).observeOn(AndroidSchedulers.mainThread())
            .subscribe(handler, onError, onCompleted)

        trackListener(listenerTag, subscription)

        return subscription
    }

    fun <T> register(owner:LifecycleOwner,oClass: Class<T>,observer: Observer<T>){
        owner.lifecycle.addObserver(OwnerLifeCycleObserver(owner))
        observerTracker.put(owner,observer)
        val liveData: MutableLiveData<Event<T>>
        if(liveDataMap.containsKey(oClass)){
            liveData = liveDataMap[oClass] as MutableLiveData<Event<T>>
        }
        else
            liveDataMap.put(oClass,MutableLiveData<Event<T>>())
    }

    fun post(event: Any) {
        subject.onNext(event)
    }

    fun removeListener(listener: Any) {
        if (listenersTracker.containsKey(listener)) {
            for (subscription in listenersTracker[listener]!!) {
                subscription.unsubscribe()
            }

            listenersTracker.remove(listener)
        }
    }

    private fun trackListener(listener: Any, subscription: Subscription) {
        if (!listenersTracker.containsKey(listener)) {
            listenersTracker[listener] = HashSet()
        }
        listenersTracker[listener]!!.add(subscription)
    }

    fun sendFinishedDetectionWithNotificationEvent(faceCount:Int, total:Int){
        post(OnFinishedDetectionWithNotification(faceCount,total))
    }

    fun sendFinishedDetectionWithDialogEvent(faceCount:Int, total:Int){
        post(OnFinishedDetectionWithDialog(faceCount,total))
    }

    class OwnerLifeCycleObserver(var owner: LifecycleOwner?) : LifecycleObserver{

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun release(){
            observerTracker.remove(owner)
            owner = null
        }
    }
}