package iam.immlkit.facedetector.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import iam.immlkit.facedetector.R
import iam.immlkit.facedetector.model.rxbus.RxBus
import iam.immlkit.facedetector.view.service.detection.DetectionService

class NotificationUtils {

    companion object{

        val NOTIF_ID = 1 //we gonna use only one notification in this demo for simplicity

        /**
         * Create notification channel for android 8+
         */
        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val serviceChannel = NotificationChannel(
                    DetectionService.CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
                )

                val manager = context.getSystemService(NotificationManager::class.java)
                manager.createNotificationChannel(serviceChannel)
            }
        }

        /**
         * Build Notification object
         */
        fun getNotification(context: Context,title:String,text:String,iconRes:Int) = NotificationCompat.Builder(context, DetectionService.CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(iconRes)
            .setOngoing(false)
            .build()

        fun showNotification(context: Context,notification: Notification){
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIF_ID, notification)
        }
    }


}