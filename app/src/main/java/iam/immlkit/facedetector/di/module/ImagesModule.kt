package iam.immlkit.facedetector.di.module

import iam.immlkit.facedetector.repos.images.AllImagesRepository
import iam.immlkit.facedetector.repos.images.FaceImagesRepository
import iam.immlkit.facedetector.repos.images.ImageRepository
import iam.immlkit.facedetector.repos.images.NonFacesImagesRepository
import iam.immlkit.facedetector.viewmodel.images.AllImagesViewModel
import iam.immlkit.facedetector.viewmodel.images.FaceImagesViewModel
import iam.immlkit.facedetector.viewmodel.images.NonFacesImagesViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module

val imagesModule = module {
    factory{ AllImagesViewModel(androidApplication(), get(StringQualifier("allImages")))}
    factory{ FaceImagesViewModel(androidApplication(), get(StringQualifier("facesImages")))}
    factory{ NonFacesImagesViewModel(androidApplication(), get(StringQualifier("nonFacesImages")))}
    factory<ImageRepository>(StringQualifier("allImages")) {AllImagesRepository(get())}
    factory<ImageRepository>(StringQualifier("facesImages")) {FaceImagesRepository(get())}
    factory<ImageRepository>(StringQualifier("nonFacesImages")) {NonFacesImagesRepository(get())}
}