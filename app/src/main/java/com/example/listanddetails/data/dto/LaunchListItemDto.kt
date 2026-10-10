package com.example.listanddetails.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LaunchListItemDto(
    val id: String,
    val name: String,
    val status: LaunchStatusDto?,
    val net: String?,
    @SerialName("lsp_name") val lspName: String? = null,
    @SerialName("launch_service_provider") val provider: LaunchProviderDto? = null,
    val image: String?,
    val pad: PadDto? = null,
    val mission: MissionDto? = null,
    @SerialName("webcast_live") val webcastLive: Boolean? = null,
    @SerialName("vid_urls") val vidUrlsSnake: List<VidUrlDto>? = null,
    @SerialName("vidURLs") val vidUrls: List<VidUrlDto>? = null
)
