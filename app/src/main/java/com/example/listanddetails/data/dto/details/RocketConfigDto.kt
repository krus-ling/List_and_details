package com.example.listanddetails.data.dto.details

import com.google.gson.annotations.SerializedName

data class RocketConfigDto(
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?
)
