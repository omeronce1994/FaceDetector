package iam.immlkit.facedetector.viewmodel.detection

import android.app.Application
import android.content.Context
import iam.immlkit.facedetector.repos.detection.DetectionRepository
import iam.immlkit.facedetector.viewmodel.BaseViewModel
import androidx.core.content.ContextCompat
import android.content.Intent
import iam.immlkit.facedetector.utils.GeneralUtils
import iam.immlkit.facedetector.view.service.detection.DetectionService


class DetectionViewModel(application: Application, private val repository: DetectionRepository) : BaseViewModel(application) {

    //initial load from disk
    fun loadFromDisk(){
        repository.loadFromDisk()
    }

    fun detect(context: Context){
        startService(context)
    }

    //start service for face detection so that detection will continue even if user killed the app
    fun startService(context: Context) {
        val serviceIntent = Intent(context, DetectionService::class.java)

        //for android 8+ start foreground
        if(GeneralUtils.runForegroundService())
            ContextCompat.startForegroundService(context, serviceIntent)
        else
            context.startService(serviceIntent)
    }

}