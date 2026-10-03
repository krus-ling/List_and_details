package com.example.listanddetails.data.mapper

import com.example.listanddetails.data.dto.LaunchDetailDto
import com.example.listanddetails.data.dto.LaunchListItemDto
import com.example.listanddetails.domain.model.LaunchDetail
import com.example.listanddetails.domain.model.LaunchItem
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LaunchListItemDto.toDomain(): LaunchItem {
    val videos = when {
        !vidUrls.isNullOrEmpty() -> vidUrls
        !vidUrlsSnake.isNullOrEmpty() -> vidUrlsSnake
        !mission?.vidUrls.isNullOrEmpty() -> mission.vidUrls
        !mission?.vidUrlsSnake.isNullOrEmpty() -> mission.vidUrlsSnake
        else -> null
    }
    val hasValidVideo = webcastLive == true || videos?.any { !it.url.isNullOrBlank() } == true

    return LaunchItem(
        id = id,
        name = name,
        status = status?.name ?: "Неизвестно",
        date = formatLaunchDate(net),
        agency = provider?.name ?: lspName ?: "Не указано",
        imageUrl = image,
        padName = pad?.name,
        hasVideo = hasValidVideo,
        rawDate = net
    )
}

fun LaunchDetailDto.toDomain(): LaunchDetail = LaunchDetail(
    id = id,
    name = name,
    status = status?.name ?: "Неизвестно",
    date = formatLaunchDate(net),
    agency = provider?.name ?: "Не указано",
    imageUrl = image,
    rocketName = rocket?.configuration?.name ?: "Неизвестная ракета",
    rocketImage = rocket?.configuration?.imageUrl,
    missionName = mission?.name ?: "Неизвестная миссия",
    missionDescription = mission?.description ?: "Не указана",
    padName = pad?.name ?: "Не указана",
    location = pad?.location?.name,
    orbit = mission?.orbit?.name,
    missionType = mission?.type ?: "Не указан",
    failReason = failReason,
    videoUrl = vidUrls?.firstOrNull()?.url,
    rawDate = net
)

private val russianDateFormatter: DateTimeFormatter by lazy {
    DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale.forLanguageTag("ru"))
        .withZone(ZoneId.systemDefault())
}

private fun formatLaunchDate(isoDate: String?): String {
    if (isoDate.isNullOrBlank()) return "Не указана"
    return try {
        val instant = Instant.parse(isoDate)
        russianDateFormatter.format(instant)
    } catch (e: Exception) {
        isoDate
    }
}