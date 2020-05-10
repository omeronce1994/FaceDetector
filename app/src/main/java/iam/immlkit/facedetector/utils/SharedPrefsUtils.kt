package iam.immlkit.facedetector.utils

import android.content.Context

class SharedPrefsUtils(context: Context) {
    private val preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
}