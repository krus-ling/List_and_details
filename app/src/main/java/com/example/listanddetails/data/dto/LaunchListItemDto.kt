package com.example.listanddetails.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LaunchListItemDto(
    val id: String,
    val name: String,
    val status: LaunchStatusDto?,
    val net: String?,
    @SerialName("lsp_name") val lspName: String?,
    val image: String?
)
