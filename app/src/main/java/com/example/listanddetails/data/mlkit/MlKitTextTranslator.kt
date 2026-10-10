package com.example.listanddetails.data.mlkit

import com.example.listanddetails.domain.repository.TextTranslator
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MlKitTextTranslator : TextTranslator {
    private val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.RUSSIAN)
        .build()

    private val englishRussianTranslator: Translator = Translation.getClient(options)

    /**
     * Скачивает языковой пакет (если еще не скачан) и переводит текст
     */
    override suspend fun translate(text: String): Result<String> = runCatching {
        if (text.isBlank()) return@runCatching text

        // Проверяем / скачиваем языковую модель при необходимости
        downloadModelIfNeeded()

        // Выполняем перевод
        suspendCancellableCoroutine { continuation ->
            englishRussianTranslator.translate(text)
                .addOnSuccessListener { translatedText ->
                    continuation.resume(translatedText)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    private suspend fun downloadModelIfNeeded() {
        val conditions = DownloadConditions.Builder()
            .build()

        suspendCancellableCoroutine { continuation ->
            englishRussianTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    fun close() {
        englishRussianTranslator.close()
    }
}