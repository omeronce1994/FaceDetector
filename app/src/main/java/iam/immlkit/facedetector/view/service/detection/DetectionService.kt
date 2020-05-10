package iam.immlkit.facedetector.view.service.detection

import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleService
import iam.immlkit.facedetector.R
import iam.immlkit.facedetector.model.Result
import iam.immlkit.facedetector.model.rxbus.LiveDataEventBus
import iam.immlkit.facedetector.model.rxbus.RxBus
import iam.immlkit.facedetector.model.vision.face.FaceResults
import iam.immlkit.facedetector.repos.detection.DetectionRepository
import iam.immlkit.facedetector.utils.GeneralUtils
import iam.immlkit.facedetector.utils.NotificationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import omeronce.android.smartactivitymanager.SmartActivityManager
import omeronce.android.smartactivitymanager.enums.AppState
import omeronce.android.smartactivitymanager.ktextensions.AppStateObserver
import org.koin.android.ext.android.get


class DetectionService : LifecycleService() {

    companion object{

        val TAG = "DetectionService"

        val CHANNEL_ID = "ForegroundServiceChannel"
        val NOTIF_ID_DETETCTION_RESULT = 2
    }

    private val job = SupervisorJob()
    private val serviceScope = CoroutineScope(job + Dispatchers.Main)
    private var isDetecting = false;

    override fun onCreate() {
        super.onCreate()
        listenToEvents()
    }

    private fun listenToEvents() {
        SmartActivityManager.observe(this, AppStateObserver {decideForeground(it)})
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        decideForeground(SmartActivityManager.getAppState())
        startDetection()
        return START_NOT_STICKY
    }

    fun decideForeground(appState: AppState) {
        if(appState == AppState.FOREGROUND) {
            maybeStopForegound()
        }
        else {
            maybeStartForeground()
        }
    }

    private fun maybeStartForeground() {
        //build foreground notification
        NotificationUtils.createNotificationChannel(this)
        val notification = NotificationUtils.getNotification(this
            ,getString(R.string.app_name)
            ,getString(R.string.notification_detecting_text)
            ,R.mipmap.ic_launcher)
        if(GeneralUtils.runForegroundService())
            startForeground(NotificationUtils.NOTIF_ID, notification)
    }

    private fun maybeStopForegound() {
        if(GeneralUtils.runForegroundService())
            stopForeground(true)
    }

    private fun startDetection() {
        if(isDetecting) {
            Log.i(TAG, "startDetetction: failed, already detecting")
            return
        }
        isDetecting = true
        val repo = get<DetectionRepository>()
        LiveDataEventBus.post("toast")
        serviceScope.launch {
            val result = repo.detect(application)
            handleDetectionResult(result)
            stopService(Intent(this@DetectionService, DetectionService::class.java))
        }
    }

    private fun handleDetectionResult(result: Result<FaceResults>) {
        when(result) {
            is Result.Success -> onSuccessfulDetection(result.value)
            is Result.Error -> showNotification(getString(R.string.failed_to_complete_detection_process))
        }
    }

    private fun onSuccessfulDetection(faceResults: FaceResults) {
        if(SmartActivityManager.getAppState() == AppState.FOREGROUND){
            RxBus.sendFinishedDetectionWithDialogEvent(faceResults.faceCount, faceResults.total)
        }
        else {
            showNotification(getString(R.string.format_detction_results_text,faceResults.faceCount, faceResults.total), NOTIF_ID_DETETCTION_RESULT)
        }
    }

    private fun showNotification(text: String, notifId: Int = NotificationUtils.NOTIF_ID) {
        val notification = NotificationUtils.getNotification(applicationContext,getString(R.string.app_name),text,R.mipmap.ic_launcher)
        NotificationUtils.showNotification(applicationContext,notification, notifId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG,"detect: onDestroyService")
        job.cancel()
        RxBus.removeListener(TAG)
    }

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}