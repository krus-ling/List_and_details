package com.example.listanddetails.ui.list.components

import com.example.listanddetails.domain.model.LaunchItem

val mockPreviewLaunches = listOf(
    LaunchItem(
        id = "1",
        name = "Falcon 1 | DemoSat",
        status = "Launch Failure",
        date = "21 марта 2007, 01:10",
        agency = "SpaceX",
        imageUrl = "https://thespacedevs-prod.nyc3.digitaloceanspaces.com/media/images/falcon_image_20190222030438.jpeg",
        padName = "Omelek Island",
        hasVideo = true,
        rawDate = "2007-03-21T01:10:00Z"
    ),
    LaunchItem(
        id = "2",
        name = "Falcon 9 Block 5 | Starlink Group 6-58",
        status = "Success",
        date = "15 мая 2024, 18:30",
        agency = "SpaceX",
        imageUrl = "https://thespacedevs-prod.nyc3.digitaloceanspaces.com/media/images/falcon_9_image_20230807133459.jpeg",
        padName = "Cape Canaveral SFS, FL, USA",
        hasVideo = true,
        rawDate = "2024-05-15T18:30:00Z"
    ),
    LaunchItem(
        id = "3",
        name = "Sputnik 8A91 | D-1 1",
        status = "Launch Failure",
        date = "27 апреля 1958, 07:00",
        agency = "Soviet Space Program",
        imageUrl = null,
        padName = "Baikonur Cosmodrome",
        hasVideo = false,
        rawDate = "1958-04-27T07:00:00Z"
    )
)