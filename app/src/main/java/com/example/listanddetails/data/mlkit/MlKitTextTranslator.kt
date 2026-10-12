package com.example.listanddetails.data.mlkit

import com.example.listanddetails.data.util.runCatchingCancellable
import com.example.listanddetails.domain.repository.TextTranslator
import com.google.android.gms.tasks.Task
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
    override suspend fun translate(text: String): Result<String> = runCatchingCancellable {
        if (text.isBlank()) return@runCatchingCancellable text

        // Проверяем / скачиваем языковую модель при необходимости
        downloadModelIfNeeded()

        // Выполняем перевод
        englishRussianTranslator.translate(text).awaitTask()
    }

    private suspend fun downloadModelIfNeeded() {
        val conditions = DownloadConditions.Builder().build()
        englishRussianTranslator.downloadModelIfNeeded(conditions).awaitTask()
    }

    override fun close() {
        englishRussianTranslator.close()
    }
}

private suspend fun <T> Task<T>.awaitTask(): T = suspendCancellableCoroutine { continuation ->
    addOnSuccessListener { result ->
        if (continuation.isActive) {
            try {
                continuation.resume(result)
            } catch (_: IllegalStateException) {
                // Игнорируем повторный resume, если корутина отменена в перерыве
            }
        }
    }
    addOnFailureListener { exception ->
        if (continuation.isActive) {
            try {
                continuation.resumeWithException(exception)
            } catch (_: IllegalStateException) {
                // Игнорируем повторный resume, если корутина отменена в перерыве
            }
        }
    }
}