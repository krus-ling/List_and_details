package com.example.listanddetails.ui.list

import com.example.listanddetails.domain.model.DateSortOrder

val DateSortOrder.title: String
    get() = when (this) {
        DateSortOrder.NEWEST_FIRST -> "Сначала новые"
        DateSortOrder.OLDEST_FIRST -> "Сначала старые"
    }

object LaunchFilterOptions {
    val statuses = listOf(
        3 to "Успешные",
        4 to "Аварии",
        7 to "Частичный сбой",
        1 to "Запланированные",
        2 to "В ожидании (TBD)",
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
        "Firefly Aerospace" to "Firefly Aerospace",
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
        "Wenchang" to "Вэньчан, Китай",
    )

    val years = (2026 downTo 2015).map { it.toString() } + listOf("2010", "2005", "2000", "1990", "1980", "1969", "1957")
}
