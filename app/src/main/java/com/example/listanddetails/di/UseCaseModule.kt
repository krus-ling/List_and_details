package com.example.listanddetails.di

import com.example.listanddetails.domain.usecase.GetAgenciesUseCase
import com.example.listanddetails.domain.usecase.GetAstronautsUseCase
import com.example.listanddetails.domain.usecase.GetFavoritesUseCase
import com.example.listanddetails.domain.usecase.GetLaunchDetailUseCase
import com.example.listanddetails.domain.usecase.GetLaunchesUseCase
import com.example.listanddetails.domain.usecase.ObserveIsFavoriteUseCase
import com.example.listanddetails.domain.usecase.ToggleFavoriteUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::GetLaunchesUseCase)
    factoryOf(::GetAgenciesUseCase)
    factoryOf(::GetAstronautsUseCase)
    factoryOf(::GetLaunchDetailUseCase)
    factoryOf(::GetFavoritesUseCase)
    factoryOf(::ObserveIsFavoriteUseCase)
    factoryOf(::ToggleFavoriteUseCase)
}
