package com.example.listanddetails.di

import com.example.listanddetails.data.util.ImageStorageManager
import com.example.listanddetails.data.util.ResumeFileDownloader
import com.example.listanddetails.ui.agencies.AgencyListViewModel
import com.example.listanddetails.ui.astronauts.AstronautListViewModel
import com.example.listanddetails.ui.details.LaunchDetailViewModel
import com.example.listanddetails.ui.favorites.FavoritesViewModel
import com.example.listanddetails.ui.list.LaunchListViewModel
import com.example.listanddetails.ui.profile.EditProfileViewModel
import com.example.listanddetails.ui.profile.ProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    single { ImageStorageManager(androidContext()) }
    single { ResumeFileDownloader(androidContext()) }

    viewModelOf(::LaunchListViewModel)
    viewModelOf(::AgencyListViewModel)
    viewModelOf(::AstronautListViewModel)
    viewModelOf(::FavoritesViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::EditProfileViewModel)

    viewModel { (launchId: String) ->
        LaunchDetailViewModel(
            getLaunchDetailUseCase = get(),
            launchId = launchId,
            translationManager = get(),
            observeIsFavoriteUseCase = get(),
            toggleFavoriteUseCase = get()
        )
    }
}
