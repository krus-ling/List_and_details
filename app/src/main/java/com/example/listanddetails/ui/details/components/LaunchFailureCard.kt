package com.example.listanddetails.ui.details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.listanddetails.R
import com.example.listanddetails.domain.model.LaunchDetail
import com.example.listanddetails.ui.details.LaunchDetailUiState

@Composable
fun LaunchFailureCard(
    launch: LaunchDetail,
    state: LaunchDetailUiState,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.launch_failure),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onErrorContainer
            )

            val failReasonText = if (state.isTranslated && !state.translatedFailReason.isNullOrBlank()) {
                state.translatedFailReason
            } else {
                launch.failReason?.takeIf { it.isNotBlank() }
                    ?: stringResource(R.string.failure_reason_missing)
            }

            Text(
                text = failReasonText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}