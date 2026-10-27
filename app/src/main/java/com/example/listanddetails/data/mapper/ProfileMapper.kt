package com.example.listanddetails.data.mapper

import com.example.listanddetails.data.model.ProfileDto
import com.example.listanddetails.domain.model.Profile

fun ProfileDto.toDomain(): Profile {
    return Profile(
        name = name,
        position = position,
        photoUri = photoUri,
        resumeUrl = resumeUrl
    )
}

fun Profile.toDto(): ProfileDto {
    return ProfileDto.newBuilder()
        .setName(name)
        .setPosition(position)
        .setPhotoUri(photoUri)
        .setResumeUrl(resumeUrl)
        .build()
}
