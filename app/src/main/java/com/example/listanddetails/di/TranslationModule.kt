package com.example.listanddetails.di

import com.example.listanddetails.data.mlkit.MlKitTextTranslator
import com.example.listanddetails.domain.repository.TextTranslator
import org.koin.dsl.module

val translationModule = module {
    factory<TextTranslator> { MlKitTextTranslator() }
}
