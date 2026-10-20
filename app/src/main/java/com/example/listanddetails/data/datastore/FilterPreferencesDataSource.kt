package com.example.listanddetails.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.listanddetails.domain.model.DateSortOrder
import com.example.listanddetails.domain.model.LaunchFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "launch_filter_preferences")

class FilterPreferencesDataSource(private val context: Context) {

    private object PreferencesKeys {
        val AGENCY = stringPreferencesKey("filter_agency")
        val STATUS_ID = intPreferencesKey("filter_status_id")
        val PAD = stringPreferencesKey("filter_pad")
        val YEAR = stringPreferencesKey("filter_year")
        val HAS_VIDEO_ONLY = booleanPreferencesKey("filter_has_video_only")
        val SORT_ORDER = stringPreferencesKey("filter_sort_order")
    }

    val filterFlow: Flow<LaunchFilter> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val sortOrderStr = preferences[PreferencesKeys.SORT_ORDER]
            val sortOrder = DateSortOrder.entries.find { it.apiValue == sortOrderStr }
                ?: DateSortOrder.NEWEST_FIRST

            LaunchFilter(
                agency = preferences[PreferencesKeys.AGENCY],
                statusId = preferences[PreferencesKeys.STATUS_ID],
                pad = preferences[PreferencesKeys.PAD],
                year = preferences[PreferencesKeys.YEAR],
                hasVideoOnly = preferences[PreferencesKeys.HAS_VIDEO_ONLY] ?: false,
                sortOrder = sortOrder
            )
        }

    suspend fun saveFilter(filter: LaunchFilter) {
        runCatching {
            context.dataStore.edit { preferences ->
                setOrRemove(preferences, PreferencesKeys.AGENCY, filter.agency)
                setOrRemove(preferences, PreferencesKeys.PAD, filter.pad)
                setOrRemove(preferences, PreferencesKeys.YEAR, filter.year)

                if (filter.statusId != null) {
                    preferences[PreferencesKeys.STATUS_ID] = filter.statusId
                } else {
                    preferences.remove(PreferencesKeys.STATUS_ID)
                }

                preferences[PreferencesKeys.HAS_VIDEO_ONLY] = filter.hasVideoOnly
                preferences[PreferencesKeys.SORT_ORDER] = filter.sortOrder.apiValue
            }
        }
    }

    suspend fun clearFilter() {
        runCatching {
            context.dataStore.edit { preferences ->
                preferences.clear()
            }
        }
    }

    private fun setOrRemove(preferences: MutablePreferences, key: Preferences.Key<String>, value: String?) {
        if (value != null) {
            preferences[key] = value
        } else {
            preferences.remove(key)
        }
    }
}
