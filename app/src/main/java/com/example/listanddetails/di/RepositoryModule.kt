package com.example.listanddetails.di

import com.example.listanddetails.data.repository.AgencyRepositoryImpl
import com.example.listanddetails.data.repository.AstronautRepositoryImpl
import com.example.listanddetails.data.repository.FavoriteLaunchRepositoryImpl
import com.example.listanddetails.data.repository.LaunchRepositoryImpl
import com.example.listanddetails.data.repository.ProfileRepositoryImpl
import com.example.listanddetails.domain.repository.AgencyRepository
import com.example.listanddetails.domain.repository.AstronautRepository
import com.example.listanddetails.domain.repository.FavoriteLaunchRepository
import com.example.listanddetails.domain.repository.LaunchRepository
import com.example.listanddetails.domain.repository.ProfileRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<LaunchRepository> {
        LaunchRepositoryImpl(
            apiService = get(),
            cache = get(),
            favoriteLaunchDao = get()
        )
    }

    single<AgencyRepository> {
        AgencyRepositoryImpl(
            apiService = get(),
            cache = get()
        )
    }

    single<AstronautRepository> {
        AstronautRepositoryImpl(
            apiService = get(),
            cache = get()
        )
    }

    single<FavoriteLaunchRepository> {
        FavoriteLaunchRepositoryImpl(favoriteDao = get())
    }

    single<ProfileRepository> {
        ProfileRepositoryImpl(
            dataStore = get(qualifier = named("profile"))
        )
    }
}
