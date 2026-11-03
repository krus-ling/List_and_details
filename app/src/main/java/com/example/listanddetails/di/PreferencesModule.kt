package com.example.listanddetails.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.example.listanddetails.data.datastore.FilterPreferencesDataSource
import com.example.listanddetails.data.datastore.ProfileSerializer
import com.example.listanddetails.data.model.ProfileDto
import com.example.listanddetails.data.repository.FilterPreferencesRepositoryImpl
import com.example.listanddetails.domain.repository.FilterPreferencesRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val preferencesModule = module {
    single { FilterPreferencesDataSource(androidContext()) }
    single<FilterPreferencesRepository> { FilterPreferencesRepositoryImpl(get()) }

    single<DataStore<ProfileDto>>(qualifier = named("profile")) {
        DataStoreFactory.create(
            serializer = ProfileSerializer,
            produceFile = { androidApplication().filesDir.resolve("profile.pb") }
        )
    }
}
