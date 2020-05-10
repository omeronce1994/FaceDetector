package iam.immlkit.facedetector.viewmodel.detection

import android.app.Application
import android.content.Context
import iam.immlkit.facedetector.repos.detection.DetectionRepository
import iam.immlkit.facedetector.viewmodel.BaseViewModel
import android.content.Intent
import androidx.lifecycle.viewModelScope
import iam.immlkit.facedetector.model.room.daos.AppDB
import iam.immlkit.facedetector.view.service.detection.DetectionService
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject


class DetectionViewModel(application: Application, private val repository: DetectionRepository) : BaseViewModel(application), KoinComponent {

    private val appDB: AppDB by inject()

    //initial load from disk
    fun loadFromDisk(){
        viewModelScope.launch {
            repository.loadFromDisk()
        }
    }

    fun detect(context: Context){
        startService(context)
    }

    //start service for face detection so that detection will continue even if user killed the app
    fun startService(context: Context) {
        val serviceIntent = Intent(context, DetectionService::class.java)
        context.startService(serviceIntent)
    }

    fun clearAllTables() {
        viewModelScope.launch {
            appDB.clearAll()
        }
    }
}