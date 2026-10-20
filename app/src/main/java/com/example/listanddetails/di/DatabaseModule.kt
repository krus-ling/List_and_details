package com.example.listanddetails.di

import androidx.room.Room
import com.example.listanddetails.data.local.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "launches_database.db"
        ).build()
    }

    single { get<AppDatabase>().favoriteLaunchDao() }
}