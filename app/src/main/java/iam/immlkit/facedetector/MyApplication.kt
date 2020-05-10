package iam.immlkit.facedetector

import android.app.Application
import omeronce.android.smartactivitymanager.SmartActivityManager

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        SmartActivityManager.init(this)
    }
}