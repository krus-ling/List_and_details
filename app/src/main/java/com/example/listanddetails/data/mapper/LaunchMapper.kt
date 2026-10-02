package com.example.listanddetails.data.mapper

import com.example.listanddetails.data.dto.LaunchDetailDto
import com.example.listanddetails.data.dto.LaunchListItemDto
import com.example.listanddetails.domain.model.LaunchDetail
import com.example.listanddetails.domain.model.LaunchItem
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LaunchListItemDto.toDomain(): LaunchItem = LaunchItem(
    id = id,
    name = name,
    status = status?.name ?: "Неизвестно",
    date = formatLaunchDate(net),
    agency = lspName ?: "Не указано",
    imageUrl = image
)

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

private fun formatLaunchDate(isoDate: String?): String {
    if (isoDate.isNullOrBlank()) return "Не указана"
    return try {
        val instant = Instant.parse(isoDate)
        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale("ru"))
            .withZone(ZoneId.systemDefault())
        formatter.format(instant)
    } catch (e: Exception) {
        isoDate
    }
}