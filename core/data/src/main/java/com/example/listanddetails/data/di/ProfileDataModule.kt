package com.example.listanddetails.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.example.listanddetails.data.datastore.ProfileSerializer
import com.example.listanddetails.data.model.ProfileDto
import com.example.listanddetails.data.notification.LessonAlarmScheduler
import com.example.listanddetails.data.repository.ProfileRepositoryImpl
import com.example.listanddetails.data.util.ImageStorageManager
import com.example.listanddetails.data.util.ResumeFileDownloader
import com.example.listanddetails.domain.repository.ProfileRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val profileDataModule = module {
    single<DataStore<ProfileDto>>(qualifier = named("profile")) {
        DataStoreFactory.create(
            serializer = ProfileSerializer,
            produceFile = { androidApplication().filesDir.resolve("profile.pb") }
        )
    }

    single<ProfileRepository> {
        ProfileRepositoryImpl(
            dataStore = get(qualifier = named("profile"))
        )
    }

    single { ImageStorageManager(androidContext()) }
    single { ResumeFileDownloader(androidContext()) }
    single { LessonAlarmScheduler(androidContext()) }
}
