package com.example.listanddetails.ui.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.listanddetails.R
import com.example.listanddetails.domain.model.DateSortOrder
import com.example.listanddetails.domain.model.LaunchFilter
import com.example.listanddetails.domain.model.LaunchFilterOptions

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    filter: LaunchFilter,
    onDismiss: () -> Unit,
    onApply: (LaunchFilter) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    var tempFilter by remember(filter) { mutableStateOf(filter) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.filters_setup),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    if (tempFilter.isActive) {
                        Badge { Text(tempFilter.activeCount.toString()) }
                    }
                }
            }

            HorizontalDivider()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FilterCardSection(title = stringResource(R.string.filter_sort_date)) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        DateSortOrder.entries.forEach { order ->
                            FilterChip(
                                selected = tempFilter.sortOrder == order,
                                onClick = { tempFilter = tempFilter.copy(sortOrder = order) },
                                label = { Text(order.title) }
                            )
                        }
                    }
                }

                FilterCardSection(title = stringResource(R.string.filter_launch_status)) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = tempFilter.statusId == null,
                            onClick = { tempFilter = tempFilter.copy(statusId = null) },
                            label = { Text(stringResource(R.string.filter_all)) }
                        )
                        LaunchFilterOptions.statuses.forEach { (statusKey, statusTitle) ->
                            FilterChip(
                                selected = tempFilter.statusId == statusKey,
                                onClick = { tempFilter = tempFilter.copy(statusId = statusKey) },
                                label = { Text(statusTitle) }
                            )
                        }
                    }
                }

                FilterCardSection(title = stringResource(R.string.filter_agency)) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = tempFilter.agency == null,
                            onClick = { tempFilter = tempFilter.copy(agency = null) },
                            label = { Text(stringResource(R.string.filter_all)) }
                        )
                        LaunchFilterOptions.agencies.forEach { (agencyKey, agencyTitle) ->
                            FilterChip(
                                selected = tempFilter.agency == agencyKey,
                                onClick = { tempFilter = tempFilter.copy(agency = agencyKey) },
                                label = { Text(agencyTitle) }
                            )
                        }
                    }
                }

                FilterCardSection(title = stringResource(R.string.filter_pad)) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = tempFilter.pad == null,
                            onClick = { tempFilter = tempFilter.copy(pad = null) },
                            label = { Text(stringResource(R.string.filter_all)) }
                        )
                        LaunchFilterOptions.pads.forEach { (padKey, padTitle) ->
                            FilterChip(
                                selected = tempFilter.pad == padKey,
                                onClick = { tempFilter = tempFilter.copy(pad = padKey) },
                                label = { Text(padTitle) }
                            )
                        }
                    }
                }

                FilterCardSection(title = stringResource(R.string.filter_year)) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = tempFilter.year == null,
                            onClick = { tempFilter = tempFilter.copy(year = null) },
                            label = { Text(stringResource(R.string.filter_all_years)) }
                        )
                        LaunchFilterOptions.years.forEach { year ->
                            FilterChip(
                                selected = tempFilter.year == year,
                                onClick = { tempFilter = tempFilter.copy(year = year) },
                                label = { Text(year) }
                            )
                        }
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.filter_video_only),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Switch(
                            checked = tempFilter.hasVideoOnly,
                            onCheckedChange = { tempFilter = tempFilter.copy(hasVideoOnly = it) }
                        )
                    }
                }
            }

            Surface(
                tonalElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            tempFilter = LaunchFilter()
                            onReset()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.reset))
                    }
                    Button(
                        onClick = { onApply(tempFilter) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.apply))
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterCardSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            content()
        }
    }
}