package com.example.listanddetails.ui.components

import com.example.listanddetails.domain.model.AppError

fun AppError.toUserMessage(): String = when (this) {
    is AppError.AirplaneMode -> "Включён режим полёта. Отключите его в настройках устройства для доступа к сети."
    is AppError.NoInternet -> "Отсутствует подключение к интернету. Проверьте Wi-Fi или мобильную сеть."
    is AppError.Timeout -> "Сервер долго не отвечает. Попробуйте чуть позже."
    is AppError.RateLimit -> "Превышен лимит запросов (429). Подождите несколько секунд."
    is AppError.Server -> "Ошибка на сервере ($code). Повторите попытку позже."
    is AppError.Unknown -> message?.takeIf { it.isNotBlank() } ?: "Произошла непредвиденная ошибка."
}
