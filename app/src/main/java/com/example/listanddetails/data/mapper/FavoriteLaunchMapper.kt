package com.example.listanddetails.data.mapper

import com.example.listanddetails.data.local.entity.FavoriteLaunchEntity
import com.example.listanddetails.domain.model.LaunchDetail
import com.example.listanddetails.domain.model.LaunchItem

fun FavoriteLaunchEntity.toDomain(): LaunchItem {
    return LaunchItem(
        id = id,
        name = name,
        status = status,
        date = date,
        agency = agency,
        imageUrl = imageUrl,
        padName = padName,
        hasVideo = hasVideo,
        rawDate = rawDate
    )
}

fun FavoriteLaunchEntity.toDetail(): LaunchDetail {
    return LaunchDetail(
        id = id,
        name = name,
        status = status,
        date = date,
        agency = agency,
        imageUrl = imageUrl,
        rocketName = rocketName ?: name,
        rocketImage = rocketImage ?: imageUrl,
        missionName = missionName ?: name,
        missionDescription = missionDescription,
        padName = padName,
        location = location,
        orbit = orbit,
        missionType = missionType,
        failReason = failReason,
        videoUrl = videoUrl,
        rawDate = rawDate
    )
}

fun LaunchItem.toFavoriteEntity(): FavoriteLaunchEntity {
    return FavoriteLaunchEntity(
        id = id,
        name = name,
        status = status,
        date = date,
        agency = agency,
        imageUrl = imageUrl,
        padName = padName,
        hasVideo = hasVideo,
        rawDate = rawDate
    )
}

fun LaunchDetail.toFavoriteEntity(): FavoriteLaunchEntity {
    return FavoriteLaunchEntity(
        id = id,
        name = name,
        status = status,
        date = date,
        agency = agency,
        imageUrl = imageUrl,
        padName = padName,
        hasVideo = !videoUrl.isNullOrBlank(),
        rawDate = rawDate,
        rocketName = rocketName,
        rocketImage = rocketImage,
        missionName = missionName,
        missionDescription = missionDescription,
        location = location,
        orbit = orbit,
        missionType = missionType,
        failReason = failReason,
        videoUrl = videoUrl
    )
}
