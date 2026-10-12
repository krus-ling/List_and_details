package com.example.listanddetails.ui.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.listanddetails.R

@Composable
fun LaunchListErrorView(
    error: String,
    cooldownSeconds: Int,
    initialCooldownSeconds: Int,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val elapsedSeconds = initialCooldownSeconds - cooldownSeconds
    val canRetry = cooldownSeconds == 0 || elapsedSeconds >= 10

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = error,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )

        if (cooldownSeconds > 0) {
            Text(
                text = stringResource(R.string.wait_cooldown, cooldownSeconds),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Button(
            onClick = onRetry,
            enabled = canRetry,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                if (cooldownSeconds > 0) {
                    stringResource(R.string.retry_cooldown, cooldownSeconds)
                } else {
                    stringResource(R.string.retry)
                }
            )
        }
    }
}