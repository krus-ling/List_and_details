package com.example.listanddetails.domain.model

sealed interface AppError {
    data object AirplaneMode : AppError
    data object NoInternet : AppError
    data object Timeout : AppError
    data class RateLimit(val cooldownSeconds: Int) : AppError
    data class Server(val code: Int) : AppError
    data class Unknown(val message: String?) : AppError
}
