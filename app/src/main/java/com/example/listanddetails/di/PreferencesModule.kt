package com.example.listanddetails.di

import com.example.listanddetails.data.datastore.FilterPreferencesDataSource
import com.example.listanddetails.data.repository.FilterPreferencesRepositoryImpl
import com.example.listanddetails.domain.repository.FilterPreferencesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val preferencesModule = module {
    single { FilterPreferencesDataSource(androidContext()) }
    single<FilterPreferencesRepository> { FilterPreferencesRepositoryImpl(get()) }
}