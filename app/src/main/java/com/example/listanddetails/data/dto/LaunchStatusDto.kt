package com.example.listanddetails.data.dto

import com.google.gson.annotations.SerializedName

data class LaunchStatusDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("abbrev") val abbrev: String?,
)
