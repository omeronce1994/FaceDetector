package iam.immlkit.facedetector.view.service.detection

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import iam.immlkit.facedetector.R
import iam.immlkit.facedetector.model.ServiceLocator
import iam.immlkit.facedetector.model.rxbus.RxBus
import iam.immlkit.facedetector.model.rxbus.events.OnFinishedDetectionWithNotification
import iam.immlkit.facedetector.utils.GeneralUtils
import iam.immlkit.facedetector.utils.NotificationUtils
import rx.functions.Action1


class DetectionService : Service() {

    companion object{

        val TAG = "DetectionService"

        val CHANNEL_ID = "ForegroundServiceChannel"
    }


    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val repo = ServiceLocator.getDetectionRepository(ServiceLocator.getAppDB(this),ServiceLocator.getFaceDetectionProcessor())

        //build foreground notification
        NotificationUtils.createNotificationChannel(this)

        val notification = NotificationUtils.getNotification(this
            ,getString(R.string.app_name)
            ,getString(R.string.notification_detecting_text)
            ,R.mipmap.ic_launcher)

        if(GeneralUtils.runForegroundService())
            startForeground(1, notification)

        listenForEvents()

        //start detection process
        repo.detect(application)

        return START_NOT_STICKY
    }

    private fun listenForEvents(){
        //show notification when finish detection
        RxBus.listenFor(TAG,OnFinishedDetectionWithNotification::class.java,object : Action1<OnFinishedDetectionWithNotification>{
            override fun call(event: OnFinishedDetectionWithNotification?) {
                val faceCount = event?.faceCount ?:0
                val total = event?.totalDetected ?:0
                val text = getString(R.string.format_detction_results_text,faceCount,total)
                Log.i(TAG,"listenForEvents: onFinishedDetection: ${text}")
                val notification = NotificationUtils.getNotification(applicationContext,getString(R.string.app_name),text,R.mipmap.ic_launcher)
                NotificationUtils.showNotification(applicationContext,notification)
                if(GeneralUtils.runForegroundService())
                    stopForeground(false)
                else
                    stopSelf()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG,"detect: onDestroyService")
        RxBus.removeListener(TAG)
    }

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}