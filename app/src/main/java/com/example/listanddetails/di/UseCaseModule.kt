package com.example.listanddetails.di

import com.example.listanddetails.domain.usecase.GetLaunchesUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::GetLaunchesUseCase)
}
