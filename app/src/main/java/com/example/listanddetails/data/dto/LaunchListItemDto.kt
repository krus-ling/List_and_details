package com.example.listanddetails.data.dto

import com.google.gson.annotations.SerializedName


data class LaunchListItemDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: LaunchStatusDto?,
    @SerializedName("net") val net: String?,
    @SerializedName("lsp_name") val lspName: String?,
    @SerializedName("image") val image: String?
)
