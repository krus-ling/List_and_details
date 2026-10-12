package com.example.listanddetails.ui.details.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.listanddetails.ui.theme.OnSuccessGreen
import com.example.listanddetails.ui.theme.SuccessGreen

@Composable
fun LaunchStatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val isSuccess = status.contains("Success", ignoreCase = true)
    val isFailure = status.contains("Fail", ignoreCase = true)

    val containerColor = when {
        isSuccess -> SuccessGreen.copy(alpha = 0.15f)
        isFailure -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val labelColor = when {
        isSuccess -> OnSuccessGreen
        isFailure -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    SuggestionChip(
        onClick = {},
        label = {
            Text(
                text = status,
                fontWeight = FontWeight.Medium,
                color = labelColor
            )
        },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = containerColor
        ),
        border = null,
        modifier = modifier
    )
}