package com.example.listanddetails.data.dto.details

import com.google.gson.annotations.SerializedName

data class PadDto(
    @SerializedName("name") val name: String?,
    @SerializedName("location") val location: LocationDto?
)
