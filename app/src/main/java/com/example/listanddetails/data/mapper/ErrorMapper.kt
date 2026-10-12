package com.example.listanddetails.data.mapper

import com.example.listanddetails.data.network.AirplaneModeException
import com.example.listanddetails.data.network.NoConnectivityException
import com.example.listanddetails.domain.model.AppError
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private const val DEFAULT_COOLDOWN_SECONDS = 30

fun Throwable.toAppError(): AppError {
    return when(this) {
        is AirplaneModeException -> AppError.AirplaneMode

        is NoConnectivityException,
        is UnknownHostException,
        is ConnectException -> AppError.NoInternet

        is SocketTimeoutException -> AppError.Timeout

        is HttpException -> when(code()) {
            429 -> {
                val retryAfter = response()?.headers()?.get("Retry-After")?.toIntOrNull()
                AppError.RateLimit(cooldownSeconds = retryAfter ?: DEFAULT_COOLDOWN_SECONDS)
            }
            in 500..599 -> AppError.Server(code())
            else -> AppError.Unknown(message())
        }

        is IOException -> AppError.NoInternet

        else -> AppError.Unknown(localizedMessage)
    }
}
