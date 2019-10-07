package iam.immlkit.facedetector.utils

import android.os.Build

class PermissionsUtils {

    companion object {
        fun isPermissionCheckNeeded() = Build.VERSION.SDK_INT>=Build.VERSION_CODES.M
    }
}