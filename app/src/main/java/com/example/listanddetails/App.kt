package com.example.listanddetails

import android.app.Application
import com.example.listanddetails.di.networkModule
import com.example.listanddetails.di.presentationModule
import com.example.listanddetails.di.repositoryModule
import com.example.listanddetails.di.translationModule
import com.example.listanddetails.di.useCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                networkModule,
                repositoryModule,
                useCaseModule,
                presentationModule,
                translationModule
            )
        }
    }
}