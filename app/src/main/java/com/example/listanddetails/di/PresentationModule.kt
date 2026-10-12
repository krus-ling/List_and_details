package com.example.listanddetails.di

import com.example.listanddetails.ui.agencies.AgencyListViewModel
import com.example.listanddetails.ui.astronauts.AstronautListViewModel
import com.example.listanddetails.ui.details.LaunchDetailViewModel
import com.example.listanddetails.ui.list.LaunchListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::LaunchListViewModel)
    viewModelOf(::AgencyListViewModel)
    viewModelOf(::AstronautListViewModel)

    viewModel { (launchId: String) ->
        LaunchDetailViewModel(
            repository = get(),
            launchId = launchId,
            translationManager = get()
        )
    }
}