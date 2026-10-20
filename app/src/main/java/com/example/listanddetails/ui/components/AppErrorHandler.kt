package com.example.listanddetails.ui.components

import com.example.listanddetails.data.mapper.toAppError
import com.example.listanddetails.domain.model.AppError

data class ErrorStateResult(
    val errorMessage: String,
    val cooldownSeconds: Int = 0,
)

fun Throwable.handleAppError(): ErrorStateResult {
    val appError = this.toAppError()
    val errorMessage = appError.toUserMessage()

    val cooldownSeconds = if (appError is AppError.RateLimit) {
        appError.cooldownSeconds
    } else {
        0
    }

    return ErrorStateResult(
        errorMessage = errorMessage,
        cooldownSeconds = cooldownSeconds,
    )
}
