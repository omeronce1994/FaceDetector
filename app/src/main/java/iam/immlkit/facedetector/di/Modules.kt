package iam.immlkit.facedetector.di

import iam.immlkit.facedetector.di.module.dbModule
import iam.immlkit.facedetector.di.module.detectionModule
import iam.immlkit.facedetector.di.module.imagesModule
import iam.immlkit.facedetector.di.module.prefsModule

val modules = listOf(prefsModule, dbModule, detectionModule, imagesModule)