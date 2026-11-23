package com.example.listanddetails.domain.di

import com.example.listanddetails.domain.usecase.GetProfileUseCase
import com.example.listanddetails.domain.usecase.SaveProfileUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val profileDomainModule = module {
    factoryOf(::GetProfileUseCase)
    factoryOf(::SaveProfileUseCase)
}
