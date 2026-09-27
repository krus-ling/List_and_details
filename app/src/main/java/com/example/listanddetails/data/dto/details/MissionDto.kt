package com.example.listanddetails.data.dto.details

import com.google.gson.annotations.SerializedName

data class MissionDto(
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("type") val type: String?
)