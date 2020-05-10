package iam.immlkit.facedetector

import android.app.Application
import iam.immlkit.facedetector.di.modules
import omeronce.android.smartactivitymanager.SmartActivityManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        SmartActivityManager.init(this)
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(modules)
        }
    }
}