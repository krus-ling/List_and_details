package com.example.listanddetails.data.dto.details

import com.google.gson.annotations.SerializedName

data class RocketDto(
    @SerializedName("configuration") val configuration: RocketConfigDto?
)
