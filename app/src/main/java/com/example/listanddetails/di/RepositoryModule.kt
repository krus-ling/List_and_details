package com.example.listanddetails.di

import com.example.listanddetails.data.repository.LaunchRepositoryImpl
import com.example.listanddetails.domain.repository.LaunchRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<LaunchRepository> {
        LaunchRepositoryImpl(apiService = get())
    }
}