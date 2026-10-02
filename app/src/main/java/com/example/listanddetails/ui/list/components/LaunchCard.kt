package com.example.listanddetails.ui.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.listanddetails.domain.model.LaunchItem
import com.example.listanddetails.ui.theme.ListAndDetailsTheme

@Composable
fun LaunchCard(
    launch: LaunchItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {
        AsyncImage(
            model = launch.imageUrl,
            contentDescription = launch.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        )

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = launch.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = launch.agency,
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = launch.date,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = launch.status,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LaunchCardPreview() {
    ListAndDetailsTheme {
        LaunchCard(
            launch = mockLaunches.first(),
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

val mockLaunches = listOf(
    // Запуск из твоего JSON (Falcon 1 | DemoSat)
    LaunchItem(
        id = "9a50023f-07fe-45ac-9ac6-7fb6b5ea970d",
        name = "Falcon 1 | DemoSat",
        status = "Launch Failure",
        date = "21 марта 2007, 01:10",
        agency = "SpaceX",
        imageUrl = "https://thespacedevs-prod.nyc3.digitaloceanspaces.com/media/images/falcon_image_20190222030438.jpeg"
    ),
    // Успешный пуск с длинным названием
    LaunchItem(
        id = "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
        name = "Falcon 9 Block 5 | Starlink Group 6-58 (Direct to Cell)",
        status = "Success",
        date = "15 мая 2024, 18:30",
        agency = "SpaceX",
        imageUrl = "https://thespacedevs-prod.nyc3.digitaloceanspaces.com/media/images/falcon_9_image_20230807133459.jpeg"
    ),
    // Пуск без картинки (проверка отображения при imageUrl = null)
    LaunchItem(
        id = "b2c3d4e5-f6a7-8901-bcde-f12345678901",
        name = "Electron | There and Back Again",
        status = "To Be Determined",
        date = "2 мая 2022, 22:49",
        agency = "Rocket Lab",
        imageUrl = null
    )
)