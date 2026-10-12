package com.example.listanddetails.di

import com.example.listanddetails.data.repository.AgencyRepositoryImpl
import com.example.listanddetails.data.repository.AstronautRepositoryImpl
import com.example.listanddetails.data.repository.LaunchRepositoryImpl
import com.example.listanddetails.domain.repository.AgencyRepository
import com.example.listanddetails.domain.repository.AstronautRepository
import com.example.listanddetails.domain.repository.LaunchRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<LaunchRepository> {
        LaunchRepositoryImpl(apiService = get())
    }

    single<AgencyRepository> {
        AgencyRepositoryImpl(apiService = get())
    }

    single<AstronautRepository> {
        AstronautRepositoryImpl(apiService = get())
    }
}