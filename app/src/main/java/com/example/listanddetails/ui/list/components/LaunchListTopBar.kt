package com.example.listanddetails.ui.list.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.listanddetails.R
import com.example.listanddetails.ui.list.LaunchFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchListTopBar(
    filter: LaunchFilter,
    isLoading: Boolean,
    hasItems: Boolean,
    onOpenFilter: () -> Unit
) {
    Column {
        TopAppBar(
            title = { Text(stringResource(R.string.app_title)) },
            actions = {
                TextButton(onClick = onOpenFilter) {
                    if (filter.isActive) {
                        Badge { Text(filter.activeCount.toString()) }
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    Text(stringResource(R.string.filters_title), fontWeight = FontWeight.Bold)
                }
            }
        )
        AnimatedVisibility(visible = isLoading && hasItems) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}