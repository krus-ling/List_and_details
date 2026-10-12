package com.example.listanddetails.ui.list.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.listanddetails.R
import com.example.listanddetails.ui.components.SearchBarTopBar

@Composable
fun LaunchListTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isLoading: Boolean,
    hasItems: Boolean,
    modifier: Modifier = Modifier
) {
    SearchBarTopBar(
        title = stringResource(R.string.app_title),
        searchQuery = searchQuery,
        onSearchQueryChange = onSearchQueryChange,
        isLoading = isLoading,
        hasItems = hasItems,
        modifier = modifier
    )
}