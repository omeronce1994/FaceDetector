package com.gabriel.chief.Managers


import android.util.Log

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

import java.util.HashMap

/**
 * This classed is used to track app activities states and in this way know app foreground and background state.
 * Register in BaseActivivy onCreate so that all app activities will be registered to this manager.
 * Using LifeCycleObserver registration is safe and each activity will unregister on it's onDestroy method
 */
class AppActivityManager private constructor() {

    companion object {

        private val TAG = "AppActivityManager"

        val instance = AppActivityManager()
    }

    private val activityMap: HashMap<String, LifecycleOwner>

    /**
     * Check if app is in foreground. (if ate least one activity state is resumed it means we are on foreground
     */
    val isAppInForeground: Boolean
        get() {
            var flag = false
            val iterator = activityMap.entries.iterator()
            while (iterator.hasNext()) {
                val current = iterator.next()
                val owner = current.value
                if (owner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    flag = true
                    break
                }
            }
            return flag
        }

    /**
     * Check if app is in killed. Basically just check if map is empty
     */
    val isAppInKilled: Boolean
        get() {
            val iterator = activityMap.entries.iterator()
            return if (iterator.hasNext())
                true
            else
                false
        }

    /**
     * If no activity is onResumed it means we are in foreground
     */
    val isAppInBackground: Boolean
        get() {
            var flag = false
            val iterator = activityMap.entries.iterator()
            while (iterator.hasNext()) {
                val current = iterator.next()
                val owner = current.value
                if (owner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    flag = true
                    break
                }
            }
            return !flag
        }

    init {
        activityMap = HashMap()
    }

    /**
     * Function used to register activity to manager
     */
    fun <T : FragmentActivity> registerOwner(owner: T) {
        Log.i(TAG, "registerOwner: " + owner.javaClass.canonicalName!!)
        val key = owner.javaClass.canonicalName
        owner.lifecycle.addObserver(OwnerObserver(owner, key))
        activityMap[key] = owner
    }

    /**
     * LifeCycleObserver to remove safely each activity when it's destroyed
     */
    inner class OwnerObserver(private var owner: LifecycleOwner?, private val key: String?) :
        LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun releaseOwner() {
            Log.i(TAG, "releaseOwner: " + key!!)
            owner!!.lifecycle.removeObserver(this)
            activityMap.remove(key)
            owner = null
        }
    }
}
