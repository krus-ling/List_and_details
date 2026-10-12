package com.example.listanddetails.data.util

import kotlinx.coroutines.CancellationException

/**
 * Безопасный аналог runCatching для корутин.
 * Пробрасывает CancellationException вверх для сохранения Structured Concurrency.
 */
inline fun <T> runCatchingCancellable(block: () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
