package com.example.listanddetails.domain.model

data class AgencyItem(
    val id: Int,
    val name: String,
    val abbrev: String,
    val type: String?,
    val countryCode: String?,
    val description: String?,
    val administrator: String?,
    val foundingYear: String?,
    val logoUrl: String?,
    val imageUrl: String?
)

data class AgencyListResult(
    val totalCount: Int,
    val items: List<AgencyItem>
)