package com.example.listanddetails.ui.profile.di

import com.example.listanddetails.ui.profile.EditProfileViewModel
import com.example.listanddetails.ui.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileFeatureModule = module {
    viewModelOf(::ProfileViewModel)
    viewModelOf(::EditProfileViewModel)
}
