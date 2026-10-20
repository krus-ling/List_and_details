package com.example.listanddetails.di

import com.example.listanddetails.data.cache.LaunchesMemoryCache
import com.example.listanddetails.domain.cache.FilterBadgeCache
import org.koin.dsl.module

val cacheModule = module {
    single { FilterBadgeCache() }
    single { LaunchesMemoryCache() }
}
