package iam.immlkit.facedetector.utils

import android.os.Build

class GeneralUtils {

    companion object{

        /**
         * Run foreGround only in android 8+
         */
        fun runForegroundService() = Build.VERSION.SDK_INT>=Build.VERSION_CODES.O
    }
}