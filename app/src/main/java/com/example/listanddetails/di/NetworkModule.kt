package com.example.listanddetails.di

import com.example.listanddetails.data.network.LaunchApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    // Kotlin serialization json
    single {
        Json {
            ignoreUnknownKeys = true // Игнорировать незнакомые поля, чтобы не падало приложение
            isLenient = true // Лояльный парсинг
            coerceInputValues = true // Подставлять дефолтные значение, если пришел null
        }
    }

    // OkHttpClient
    single {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Retrofit
    single {
        val json: Json = get()
        val okHttpClient: OkHttpClient = get()
        val contentType = "application/json".toMediaType()

        Retrofit.Builder()
            .baseUrl("https://ll.thespacedevs.com/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    // LaunchApiService
    single<LaunchApiService> {
        get<Retrofit>().create(LaunchApiService::class.java)
    }
}