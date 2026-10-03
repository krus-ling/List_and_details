package com.example.listanddetails.ui.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.listanddetails.R
import com.example.listanddetails.ui.list.LaunchFilter
import com.example.listanddetails.ui.list.LaunchFilterOptions

@Composable
fun ActiveFilterChipsRow(
    filter: LaunchFilter,
    onFilterChanged: (LaunchFilter) -> Unit,
    onResetFilter: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        if (filter.agency != null) {
            item {
                val agencyTitle = LaunchFilterOptions.agencies.find { it.first == filter.agency }?.second ?: filter.agency
                InputChip(
                    selected = true,
                    onClick = { onFilterChanged(filter.copy(agency = null)) },
                    label = { Text(agencyTitle) },
                    trailingIcon = { Icon(Icons.Default.Close, contentDescription = stringResource(R.string.cd_delete_agency_filter)) }
                )
            }
        }
        if (filter.statusId != null) {
            item {
                val statusTitle = LaunchFilterOptions.statuses.find { it.first == filter.statusId }?.second ?: filter.statusId.toString()
                InputChip(
                    selected = true,
                    onClick = { onFilterChanged(filter.copy(statusId = null)) },
                    label = { Text(statusTitle) },
                    trailingIcon = { Icon(Icons.Default.Close, contentDescription = stringResource(R.string.cd_delete_status_filter)) }
                )
            }
        }
        if (filter.year != null) {
            item {
                InputChip(
                    selected = true,
                    onClick = { onFilterChanged(filter.copy(year = null)) },
                    label = { Text(filter.year) },
                    trailingIcon = { Icon(Icons.Default.Close, contentDescription = stringResource(R.string.cd_delete_year_filter)) }
                )
            }
        }
        if (filter.pad != null) {
            item {
                val padTitle = LaunchFilterOptions.pads.find { it.first == filter.pad }?.second ?: filter.pad
                InputChip(
                    selected = true,
                    onClick = { onFilterChanged(filter.copy(pad = null)) },
                    label = { Text(padTitle) },
                    trailingIcon = { Icon(Icons.Default.Close, contentDescription = stringResource(R.string.cd_delete_pad_filter)) }
                )
            }
        }
        if (filter.hasVideoOnly) {
            item {
                InputChip(
                    selected = true,
                    onClick = { onFilterChanged(filter.copy(hasVideoOnly = false)) },
                    label = { Text(stringResource(R.string.filter_video_only)) },
                    trailingIcon = { Icon(Icons.Default.Close, contentDescription = stringResource(R.string.cd_delete_video_filter)) }
                )
            }
        }
        item {
            TextButton(onClick = onResetFilter) {
                Text(stringResource(R.string.reset_all))
            }
        }
    }
}