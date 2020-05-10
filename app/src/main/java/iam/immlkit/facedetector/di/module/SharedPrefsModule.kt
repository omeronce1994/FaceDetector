package iam.immlkit.facedetector.di.module

import iam.immlkit.facedetector.utils.SharedPrefsUtils
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val prefsModule = module {
    single { SharedPrefsUtils(androidContext()) }
}