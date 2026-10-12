package com.example.listanddetails.domain.model

enum class DateSortOrder(val apiValue: String) {
    NEWEST_FIRST("-net"),
    OLDEST_FIRST("net"),
}

data class LaunchFilter(
    val agency: String? = null,
    val statusId: Int? = null,
    val pad: String? = null,
    val year: String? = null,
    val hasVideoOnly: Boolean = false,
    val sortOrder: DateSortOrder = DateSortOrder.NEWEST_FIRST,
) {
    val isActive: Boolean
        get() = agency != null || statusId != null || pad != null || year != null || hasVideoOnly || sortOrder != DateSortOrder.NEWEST_FIRST

    val activeCount: Int
        get() = listOfNotNull(
            agency,
            statusId,
            pad,
            year,
            if (hasVideoOnly) true else null,
            if (sortOrder != DateSortOrder.NEWEST_FIRST) true else null,
        ).size
}
