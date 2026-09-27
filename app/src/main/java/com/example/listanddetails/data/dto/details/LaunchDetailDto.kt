package com.example.listanddetails.data.dto.details

import com.example.listanddetails.data.dto.LaunchStatusDto
import com.google.gson.annotations.SerializedName

data class LaunchDetailDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: LaunchStatusDto?,
    @SerializedName("net") val net: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("mission") val mission: MissionDto?,
    @SerializedName("pad") val pad: PadDto?,
    @SerializedName("rocket") val rocket: RocketDto?
)