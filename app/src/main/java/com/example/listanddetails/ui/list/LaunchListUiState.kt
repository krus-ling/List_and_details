package com.example.listanddetails.ui.list

import com.example.listanddetails.domain.model.LaunchItem

enum class DateSortOrder(val title: String, val apiValue: String) {
    NEWEST_FIRST("Сначала новые", "-net"),
    OLDEST_FIRST("Сначала старые", "net")
}

data class LaunchFilter(
    val agency: String? = null,
    val statusId: Int? = null,
    val pad: String? = null,
    val year: String? = null,
    val hasVideoOnly: Boolean = false,
    val sortOrder: DateSortOrder = DateSortOrder.NEWEST_FIRST
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
            if (sortOrder != DateSortOrder.NEWEST_FIRST) true else null
        ).size
}

object LaunchFilterOptions {
    val statuses = listOf(
        3 to "Успешные",
        4 to "Аварии",
        7 to "Частичный сбой",
        1 to "Запланированные",
        2 to "В ожидании (TBD)"
    )

    val agencies = listOf(
        "SpaceX" to "SpaceX",
        "NASA" to "NASA",
        "Roscosmos" to "Роскосмос",
        "Rocket Lab" to "Rocket Lab",
        "ESA" to "ESA (Европа)",
        "United Launch Alliance" to "ULA (США)",
        "ISRO" to "ISRO (Индия)",
        "CASC" to "CASC (Китай)",
        "Arianespace" to "Arianespace",
        "Blue Origin" to "Blue Origin",
        "Northrop Grumman" to "Northrop Grumman",
        "JAXA" to "JAXA (Япония)",
        "Firefly Aerospace" to "Firefly Aerospace"
    )

    val pads = listOf(
        "Cape Canaveral" to "Мыс Канаверал, США",
        "Kennedy" to "КЦ Кеннеди, США",
        "Baikonur" to "Байконур, Казахстан",
        "Vandenberg" to "Ванденберг, США",
        "Starbase" to "Бока-Чика (Starbase), США",
        "Kourou" to "Куру, Французская Гвиана",
        "Rocket Lab" to "Rocket Lab LC-1, Новая Зеландия",
        "Plesetsk" to "Плесецк, Россия",
        "Vostochny" to "Восточный, Россия",
        "Tanegashima" to "Танегасима, Япония",
        "Satish Dhawan" to "Сатиш Дхаван, Индия",
        "Jiuquan" to "Цзюцюань, Китай",
        "Wenchang" to "Вэньчан, Китай"
    )

    val years = (2026 downTo 2015).map { it.toString() } + listOf("2010", "2005", "2000", "1990", "1980", "1969", "1957")
}

data class LaunchListUiState(
    val isLoading: Boolean = false,
    val isNextPageLoading: Boolean = false,
    val items: List<LaunchItem> = emptyList(),
    val totalCount: Int = 0,
    val searchQuery: String = "",
    val error: String? = null,
    val cooldownSeconds: Int = 0,
    val initialCooldownSeconds: Int = 0,
    val filter: LaunchFilter = LaunchFilter(),
    val isFilterSheetOpen: Boolean = false
)