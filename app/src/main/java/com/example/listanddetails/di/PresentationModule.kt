package com.example.listanddetails.di

import com.example.listanddetails.ui.details.LaunchDetailViewModel
import com.example.listanddetails.ui.list.LaunchListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::LaunchListViewModel)

    viewModel { (launchId: String) ->
        LaunchDetailViewModel(
            repository = get(),
            launchId = launchId,
        )
    }
}