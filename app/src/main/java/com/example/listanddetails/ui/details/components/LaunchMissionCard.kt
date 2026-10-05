package com.example.listanddetails.ui.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.listanddetails.R
import com.example.listanddetails.domain.model.LaunchDetail
import com.example.listanddetails.ui.details.LaunchDetailUiState

@Composable
fun LaunchMissionCard(
    launch: LaunchDetail,
    state: LaunchDetailUiState,
    onTranslateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.mission_prefix, launch.missionName),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                if (!launch.missionDescription.isNullOrBlank()) {
                    TextButton(
                        onClick = onTranslateClick,
                        enabled = !state.isTranslating
                    ) {
                        if (state.isTranslating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = if (state.isTranslated) "Оригинал" else "Перевести",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }

            val missionTypeText = if (state.isTranslated && !state.translatedMissionType.isNullOrBlank()) {
                state.translatedMissionType
            } else {
                launch.missionType
            }
            if (!missionTypeText.isNullOrBlank()) {
                Text(
                    text = stringResource(R.string.mission_type_prefix, missionTypeText),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            val orbitText = if (state.isTranslated && !state.translatedOrbit.isNullOrBlank()) {
                state.translatedOrbit
            } else {
                launch.orbit
            }
            if (!orbitText.isNullOrBlank()) {
                Text(
                    text = stringResource(R.string.orbit_prefix, orbitText),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            val descriptionText = if (state.isTranslated && !state.translatedDescription.isNullOrBlank()) {
                state.translatedDescription
            } else {
                launch.missionDescription ?: stringResource(R.string.not_specified)
            }

            Text(
                text = descriptionText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}